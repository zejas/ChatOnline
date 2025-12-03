package com.zejas.authsvr.exception;

import lombok.Getter;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/12/2 11:29
 */
@Getter
public class AuthException extends RuntimeException {

    private final int code;
    private final String msg;

    public AuthException(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public AuthException(AuthExceptionEnum authExceptionEnum){
        this.code = authExceptionEnum.getCode();
        this.msg = authExceptionEnum.getMsg();
    }
}
