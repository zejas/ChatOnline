package com.spring.authsvr.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.locks.Lock;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/8/3 15:53
 */
@Component
public class DistributedLockFactory {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String lockName;

    private String uuid;

    DistributedLockFactory(){
        this.uuid = UUID.randomUUID().toString();
    }
    public Lock getDistributedLock(String lockType){
        if (lockType==null){
            return null;
        }else if(lockType.equalsIgnoreCase("REDIS")){
            this.lockName = "redisLock";
            return new RedisDistributedLock(stringRedisTemplate,lockName,uuid,30L);
        }else if(lockType.equalsIgnoreCase("ZOOKEEPER")){
            this.lockName = "zookeeperLockNode";
            //TODO
            return null;
        }else if (lockType.equalsIgnoreCase("MYSQL")){
            this.lockName = "mysqlLock";
            //TODO
            return null;
        }else {
            return null;
        }

    }
}
