package com.spring.authsvr.exception;

import lombok.Getter;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/12/2 11:29
 */
@Getter
public enum AuthExceptionEnum {

    AUTH_EMPTY(1001,"认证信息为空"),
    AUTH_FAILED(1002,"用户认证失败"),
    AUTH_USER_NOT_FOUND(1003,"用户账户或密码错误"),
    AUTH_ROLE_NOT_FOUND(1004,"用户未纳入权限管理");

    private final int code;
    private final String msg;

    AuthExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
