package com.spring.authsvr.common;

import lombok.Data;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/23 10:49
 */
@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    public static <T> R<T> success(T data) {
        R<T> r = new R<T>();
        r.code = 200;
        r.msg = "ok";
        r.data = data;
        return r;
    }
    public static <T> R<T> fail(Integer code, String msg, T data) {
        R<T> r = new R<T>();
        r.code = code;
        r.msg = msg;
        r.data = data;
        return r;
    }
}
