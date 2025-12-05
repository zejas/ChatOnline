package com.zejas.authsvr.service;

import com.zejas.authsvr.exception.AuthException;
import com.zejas.authsvr.exception.AuthExceptionEnum;
import com.zejas.authsvr.mapper.UserRoleMapper;
import com.zejas.authsvr.model.po.UserRole;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/15 16:55
 */
@Log4j2
@Service
public class UserRoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    public UserRole[] getUserRoleByUserId(Long userId) {
        UserRole[] userRole = userRoleMapper.getUserRoleByUserId(userId);
        if(userRole == null || userRole.length==0){
            log.info("此用户id:{}未列入权限管理",userId);
            throw new AuthException(AuthExceptionEnum.AUTH_ROLE_NOT_FOUND);
        }
        return userRole;
    }

}
