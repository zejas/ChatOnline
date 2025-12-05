package com.zejas.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.setProperty("nacos.logging.default.config.enabled","false");
    }

    @Bean
    public WebClient.Builder webclient(){
        return WebClient.builder();
    }
}
