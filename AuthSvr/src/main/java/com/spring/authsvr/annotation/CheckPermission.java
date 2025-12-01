package com.spring.authsvr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/15 16:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckPermission {
    //鉴权
    String[] permitType() default {};

}
