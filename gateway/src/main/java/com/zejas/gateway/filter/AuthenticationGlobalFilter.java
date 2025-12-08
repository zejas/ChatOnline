package com.zejas.gateway.filter;

import com.zejas.gateway.webclient.AuthResponse;
import com.zejas.gateway.webclient.AuthWebClient;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Component
public class AuthenticationGlobalFilter implements GlobalFilter {
    @Autowired
    private AuthWebClient authWebClient;

//    //gateway自动配置会将filteringWebHandler加入bean（会自动注入filter），而这个filter需要feign客户端，而feign客户端又依赖filteringWebHandler
//    @Autowired
//    @Lazy
//    private AuthFeignClient AuthFeignClient;

    //被spring容器自动订阅
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().equals("/auth/login")){
            return chain.filter(exchange);
        }
        //1.webclient调用
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
        }).timeout(Duration.ofSeconds(5),Mono.defer(()-> timeout(exchange)
        )).onErrorResume(e -> {
            log.info("网关服务调用authsvr异常：{}",e.getMessage());
            return internalError(exchange);
        });

        //2.openfeign调用
//        AuthResponse authResponse = AuthFeignClient.validate(exchange.getRequest().getHeaders().getFirst("Authorization"));
//        if (authResponse.getIsValid()){
//            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
//                    .header("X-User-Id", String.valueOf(authResponse.getUserId()))
//                    .header("X-Roles", authResponse.getRoleCode().stream().map(String::valueOf).collect(Collectors.joining(",")))
//                    .build();
//
//            return chain.filter(exchange.mutate().request(mutatedRequest).build());
//        }
//        return chain.filter(exchange);

    }
    public void isExcluded(){

    }
    public Mono<Void> internalError(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return exchange.getResponse().setComplete();
    }
    public Mono<Void> timeout(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    public Mono<Void> unauthorized(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
