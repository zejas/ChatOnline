package com.spring.authsvr.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/16 13:12
 */
@Data
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String phone;
    private String email;
    private String password;
    private String salt;
}
