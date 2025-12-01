package com.spring.authsvr.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/1 18:38
 */

@Configuration
public class RedisConfig {

    /**
     * springboot自动配置好了连接工厂
     * @param redisConnectionFactory
     * @return
     */
    @Value("${redisson.address}")
    private String address;

    @Value("${redisson.password}")
    private String password;

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,GenericJacksonJsonRedisSerializer genericJacksonJsonRedisSerializer) {
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(genericJacksonJsonRedisSerializer);
        return redisTemplate;
    }
    @Bean
    public Redisson redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress(address).setDatabase(0).setPassword(password);
        return (Redisson) Redisson.create(config);
    }
}
