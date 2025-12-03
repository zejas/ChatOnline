package com.zejas.authsvr.mapper;

import com.zejas.authsvr.model.po.UserRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/15 17:00
 */
public interface UserRoleMapper {
    UserRole[] getUserRoleByUserId(@Param("userId") Long userId);
}
