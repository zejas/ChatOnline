package com.spring.authsvr.mapper;

import com.spring.authsvr.po.User;
import com.spring.authsvr.po.UserInfo;
import org.apache.ibatis.annotations.Options;
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

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void setUser(@Param("user") User user);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void setUserInfo(@Param("userinfo") UserInfo userInfo);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void updateUserById(@Param("user") User user, @Param("id") Long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void updateUserInfoByUserId(@Param("userinfo") UserInfo userInfo, @Param("user_id") Long userId);

    void updateToken(@Param("id") Long id, @Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken );
}
