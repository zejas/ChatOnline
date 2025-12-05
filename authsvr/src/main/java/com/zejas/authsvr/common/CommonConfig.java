package com.zejas.authsvr.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/12/4 17:06
 */
@RefreshScope
@Data
@Component
public class CommonConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${redisson.address}")
    private String address;

    @Value("${redisson.password}")
    private String password;
}
