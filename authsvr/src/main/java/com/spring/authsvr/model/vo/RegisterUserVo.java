package com.spring.authsvr.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/17 10:16
 */
@Data
public class RegisterUserVo {

    @JsonProperty("user_name")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Min(0)
    @Max(140)
    @NotNull(message = "年龄不能为空")
    private Integer age;

    private Double accounts;
    private String[] hobbies;

    @NotBlank(message = "请输入用户手机号")
    private String phone;
    @Email()
    private String email;

}
