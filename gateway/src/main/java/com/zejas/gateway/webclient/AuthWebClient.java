package com.zejas.gateway.webclient;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/12/3 10:06
 */
@Component
public class AuthWebClient {

    private final WebClient webClient;

    public AuthWebClient(WebClient.Builder webclientBuilder) {
        this.webClient = webclientBuilder.build();
    }

    public Mono<AuthResponse> validate(String token){

        return webClient.get()
                .uri("http://authsvr/auth/validate")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(AuthResponse.class);
    }
}
