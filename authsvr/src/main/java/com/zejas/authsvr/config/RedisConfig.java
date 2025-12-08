package com.zejas.authsvr.config;

import com.zejas.authsvr.common.CommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

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
    @Autowired
    private CommonProperties commonProperties;

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
//    @Bean
//    public Redisson redisson(){
//        Config config = new Config();
//        config.useSingleServer().setAddress(commonProperties.getAddress()).setDatabase(0).setPassword(commonConfig.getPassword());
//        return (Redisson) Redisson.create(config);
//    }
}
