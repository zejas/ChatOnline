package com.zejas.authsvr.service;

import com.zejas.authsvr.exception.AuthException;
import com.zejas.authsvr.exception.AuthExceptionEnum;
import com.zejas.authsvr.mapper.UserRoleMapper;
import com.zejas.authsvr.model.po.UserRole;
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
