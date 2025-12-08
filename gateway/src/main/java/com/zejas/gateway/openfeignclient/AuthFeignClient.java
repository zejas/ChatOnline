package com.zejas.gateway.openfeignclient;

import com.zejas.gateway.webclient.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *  <p>  </p>
 *
 * @author ZShuo
 * @description 
 * @date 2025/12/7 18:22
 */
@FeignClient("authsvr")
public interface AuthFeignClient {
    @GetMapping("/auth/validate")
    AuthResponse validate(@RequestHeader("Authorization") String token);
}
