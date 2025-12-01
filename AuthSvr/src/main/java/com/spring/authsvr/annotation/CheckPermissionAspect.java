package com.spring.authsvr.annotation;

import com.spring.authsvr.exception.UserException;
import com.spring.authsvr.exception.UserExceptionEnum;
import com.spring.authsvr.po.UserRole;
import com.spring.authsvr.service.UserRoleService;
import com.spring.authsvr.util.TokenUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/15 16:33
 */
@Component
@Aspect
public class CheckPermissionAspect {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    TokenUtil tokenUtil;

    @Before(value = "@annotation(checkPermission)")
    public void checkPermissionBefore(CheckPermission checkPermission) {
        String[] requiredPermissions = checkPermission.permitType();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("Authorization");
        Long userId = tokenUtil.verifyAccessToken(token);

        List<UserRole> userRoles = List.of(userRoleService.getUserRoleByUserId(userId));
        Set<String> roles = userRoles.stream().map(UserRole::getRoleName).collect(Collectors.toSet());
        if(!hasPermission(requiredPermissions, roles)) {
            throw new UserException(UserExceptionEnum.USER_NOT_PERMITTED);
        }
    }

    private boolean hasPermission(String[] requiredPermissions, Set<String> roles) {
        return Arrays.stream(requiredPermissions).anyMatch(roles::contains);
    }
}
