package com.zejas.authsvr.common;

import lombok.Data;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/12/3 10:16
 */
@Data
public class AuthResponse {
    private Boolean isValid;
    private Long userId;
    private List<Integer> roleCode;

    public static AuthResponse success(Boolean isValid,Long userId,List<Integer> roleCode){
        AuthResponse response = new AuthResponse();
        response.isValid = isValid;
        response.roleCode = roleCode;
        response.userId = userId;
        return response;
    }
}
