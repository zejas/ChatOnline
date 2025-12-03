package com.spring.authsvr.mapper;

import com.spring.authsvr.model.po.User;
import com.spring.authsvr.model.po.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/23 10:32
 */

public interface UserMapper {


    UserInfo getUserInfoByUserId(@Param("userId") Long userId);

    User getUserByName(@Param("username") String username);

    User getUserById(@Param("id") Long id);

    void updateToken(@Param("id") Long id, @Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken );
}
