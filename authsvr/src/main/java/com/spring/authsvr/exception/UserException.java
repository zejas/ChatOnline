package com.spring.authsvr.exception;

import lombok.Getter;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/23 11:02
 */
@Getter
public class UserException extends RuntimeException {

    private final Integer code;
    private final String msg;

    public UserException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public UserException(UserExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.msg = exceptionEnum.getMsg();
    }
}
