package com.zejas.gateway.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/12/2 10:12
 */
@Component
@Log4j2
public class ApiTimeGlobalFilter implements GlobalFilter, Ordered {

    public static final String BEGIN_TIME="begin_time";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(BEGIN_TIME,System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            Long beginTime = exchange.getAttribute(BEGIN_TIME);
            if(beginTime != null){
                log.info("访问接口主机:{}",exchange.getRequest().getURI().getHost()+":"+exchange.getRequest().getURI().getPort());
                log.info("访问接口url:{}",exchange.getRequest().getURI().getPath());
                log.info("访问接口时长:{}",System.currentTimeMillis()-beginTime);
                log.info("=======================api===================");
            }
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
