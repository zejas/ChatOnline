package com.spring.authsvr.mapper;

import com.spring.authsvr.po.UserRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/15 17:00
 */
public interface UserRoleMapper {

    UserRole[] getUserRoleByUserId(@Param("userId") Long UserId);

}
