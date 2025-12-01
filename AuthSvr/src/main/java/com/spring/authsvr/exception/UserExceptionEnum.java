package com.spring.authsvr.exception;

import lombok.Getter;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/23 11:09
 */
@Getter
public enum UserExceptionEnum {

    USER_NOT_FOUND(1001,"没有找到用户"),
    USER_AUTHENTICATION_FAILED(1002,"用户认证失败"),
    USER_NOT_PERMITTED(1003,"此用户没有相关权限"),
    USERROLE_NOT_CONTROL(1004,"无权限，此用户没有纳入权限管理中"),
    USER_HASBEEN_CREATED(1005,"注册失败，此用户已经存在"),
    USER_AUTHENTICATION_FORMAT(1006,"验证失败，token格式错误");

    private final int code;
    private final String msg;

    UserExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
