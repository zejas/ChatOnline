package com.spring.authsvr.service;

import com.spring.authsvr.exception.AuthException;
import com.spring.authsvr.exception.AuthExceptionEnum;
import com.spring.authsvr.mapper.UserMapper;
import com.spring.authsvr.model.po.User;
import com.spring.authsvr.model.po.UserInfo;
import com.spring.authsvr.util.TokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

        User user = tryGetUserByName(username);
        if (user == null) {
            throw new AuthException(AuthExceptionEnum.AUTH_USER_NOT_FOUND);
        }
        UserInfo userInfo = userMapper.getUserInfoByUserId(user.getId());

        String requiredPass = TokenUtil.getSHA256(password,Base64.getDecoder().decode(userInfo.getSalt()));
        if (!requiredPass.equals(userInfo.getPassword())) {
            throw new AuthException(AuthExceptionEnum.AUTH_FAILED);
        }
        String accessToken = tokenUtil.generateAccessToken(user.getId(), username);
        String refreshToken = tokenUtil.generateRefreshToken(user.getId(), username);
        userMapper.updateToken(userInfo.getId(), accessToken, refreshToken);

        // 登录成功再同步到缓存
        saveUserToRedis(user);

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

    private User tryGetUserByName(String username) {
        User user = (User) redisTemplate.opsForValue().get(USER_KEY + username);
        if (user == null) {
            user = userMapper.getUserByName(username);
        }
        return user;
    }

    private void saveUserToRedis(User user) {
        if(user != null) {
            redisTemplate.opsForValue().setIfAbsent(USER_KEY + user.getUsername(), user,1L, TimeUnit.HOURS);
        }
    }

}
