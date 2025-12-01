package com.spring.authsvr.service;

import com.spring.authsvr.exception.UserException;
import com.spring.authsvr.exception.UserExceptionEnum;
import com.spring.authsvr.mapper.UserMapper;
import com.spring.authsvr.po.User;
import com.spring.authsvr.po.UserInfo;
import com.spring.authsvr.util.TokenUtil;
import com.spring.authsvr.vo.RegisterUserVo;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/23 11:01
 */
@Service
public class UserService {

    private Logger logger = LogManager.getLogger(UserService.class);

    public static final String USER_KEY = "user:";

    @Autowired
    UserMapper userMapper;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    RedisTemplate redisTemplate;

    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<String, String>();

        User user = getUserByName(username);
        if (user == null) {
            throw new UserException(UserExceptionEnum.USER_NOT_FOUND);
        }
        UserInfo userInfo = userMapper.getUserInfoByUserId(user.getId());

        String requiredPass = TokenUtil.getSHA256(password,Base64.getDecoder().decode(userInfo.getSalt()));
        if (!requiredPass.equals(userInfo.getPassword())) {
            throw new UserException(UserExceptionEnum.USER_AUTHENTICATION_FAILED);
        }
        String accessToken = tokenUtil.generateAccessToken(user.getId(), username);
        String refreshToken = tokenUtil.generateRefreshToken(user.getId(), username);
        userMapper.updateToken(userInfo.getId(), accessToken, refreshToken);

        //准备返回access_token
        map.put("access_token",accessToken);
        map.put("refresh_token",refreshToken);

        return map;
    }

    public User getUserById(Long id) {
        //优先从redis获取
        User user = (User) redisTemplate.opsForValue().get(USER_KEY + id);
        if(user == null) {
            //双检加锁机制
            synchronized (this) {
                user = (User) redisTemplate.opsForValue().get(USER_KEY + id);
                if(user == null) {
                    logger.info("redis读取失败，尝试读取rds数据库");
                    user = userMapper.getUserById(id);

                    //写回到redis
                    if(user != null) {
                        redisTemplate.opsForValue().set(USER_KEY + id, user,1L,TimeUnit.HOURS);
                    }
                }
            }
        }
        return user;
    }

    @Transactional
    public void addUser(@Valid RegisterUserVo userVo) {
        if(getUserByName(userVo.getUsername().trim()) != null) {
            throw new UserException(UserExceptionEnum.USER_HASBEEN_CREATED);
        }

        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userVo, userInfo);

        byte[] salt = TokenUtil.getSecureRandom(16);
        userInfo.setPassword(TokenUtil.getSHA256(userInfo.getPassword(), salt));
        userInfo.setSalt(Base64.getEncoder().encodeToString(salt));

        userMapper.setUser(user);

        userInfo.setUserId(user.getId());
        userMapper.setUserInfo(userInfo);

    }

    public User getUserByName(String name) {
        User user = (User)redisTemplate.opsForValue().get(USER_KEY + name);
        if(user == null) {
            //双检加锁机制
            synchronized (this) {
                user = (User)redisTemplate.opsForValue().get(USER_KEY + name);
                if(user == null) {
                    logger.info("redis读取失败，尝试读取rds数据库");
                    user = userMapper.getUserByName(name);

                    //写回到redis，使用setIfAbsent是因为sychronized锁只限于单个jvm中，如果集群部署则无法实现
                    if(user != null) {
                        redisTemplate.opsForValue().setIfAbsent(USER_KEY + name, user,1L, TimeUnit.HOURS);
                    }
                }
            }
        }
        return user;
    }

    public void updateUser(RegisterUserVo userVo, Long userId) {
        if(getUserByName(userVo.getUsername().trim()) != null) {
            throw new UserException(UserExceptionEnum.USER_HASBEEN_CREATED);
        }

        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userVo, userInfo);

        byte[] salt = TokenUtil.getSecureRandom(16);
        userInfo.setPassword(TokenUtil.getSHA256(userInfo.getPassword(), salt));
        userInfo.setSalt(Base64.getEncoder().encodeToString(salt));
        String username = userMapper.getUserById(userId).getUsername();

        //先更新数据库，再删除redis，依靠读操作更新redis
        userMapper.updateUserById(user,userId);
        redisTemplate.delete(USER_KEY + userId);
        redisTemplate.delete(USER_KEY + username);
        userMapper.updateUserInfoByUserId(userInfo,userId);
    }


}
