package com.spring.authsvr.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/28 9:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVo {

    @NotBlank(message = "用户名不能为空")
    @JsonProperty("user_name")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
