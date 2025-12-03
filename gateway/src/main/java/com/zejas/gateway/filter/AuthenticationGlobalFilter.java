package com.zejas.gateway.filter;

import com.zejas.gateway.webclient.AuthResponse;
import com.zejas.gateway.webclient.AuthWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Collectors;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/12/2 11:36
 */
@Component
public class AuthenticationGlobalFilter implements GlobalFilter {
    @Autowired
    private AuthWebClient authWebClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Mono<AuthResponse> authMono = authWebClient.validate(exchange.getRequest().getHeaders().getFirst("Authorization"));
        return authMono.flatMap(authResponse -> {
            if (authResponse.getIsValid()){
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", String.valueOf(authResponse.getUserId()))
                        .header("X-Roles", authResponse.getRoleCode().stream().map(String::valueOf).collect(Collectors.joining(",")))
                        .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }else{
                return unauthorized(exchange);
            }
        }).timeout(Duration.ofSeconds(5),Mono.defer(()->{
                exchange.getResponse().setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
                return exchange.getResponse().setComplete();
            }
        ));

    }
    public void isExcluded(){

    }

    public Mono<Void> unauthorized(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
