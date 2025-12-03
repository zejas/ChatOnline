package com.spring.authsvr.model.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/22 16:41
 */
@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -2313801644239884296L;

    private Long id;
    private String username;
    private Integer age;
    private Double accounts;
    private String[] hobbies;
}
