package com.spring.authsvr.service;

import com.spring.authsvr.exception.AuthException;
import com.spring.authsvr.exception.AuthExceptionEnum;
import com.spring.authsvr.mapper.UserRoleMapper;
import com.spring.authsvr.model.po.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/15 16:55
 */
@Service
public class UserRoleService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    UserRoleMapper userRoleMapper;

    public UserRole[] getUserRoleByUserId(Long userId) {
        UserRole[] userRole = userRoleMapper.getUserRoleByUserId(userId);
        if(userRole == null || userRole.length==0){
            logger.info("此用户id:{}未列入权限管理",userId);
            throw new AuthException(AuthExceptionEnum.AUTH_ROLE_NOT_FOUND);
        }
        return userRole;
    }

}
