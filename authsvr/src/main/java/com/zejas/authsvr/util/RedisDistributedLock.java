package com.zejas.authsvr.util;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/8/3 13:07
 */
@Log4j2
public class RedisDistributedLock implements Lock {

    private final Timer timer = new Timer(true);

    private StringRedisTemplate redisTemplate;

    private String lockKey;

    private String uuidValue;

    private Long expireTime;

    public RedisDistributedLock(StringRedisTemplate redisTemplate, String lockKey, String uuid, Long expireTime) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        this.uuidValue = uuid + Thread.currentThread().getName();
        this.expireTime = expireTime;
    }
    @Override
    public void lock() {
        tryLock();
    }

    @Override
    public void unlock() {
        //释放锁lua脚本
        String unlockScript = "if redis.call('hexists',KEYS[1],ARGV[1]) == 0 then " +
                "return nil " +
                "elseif redis.call('hincrby',KEYS[1],ARGV[1],-1) == 0 then " +
                "return redis.call('del',KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";
        Long flag = (Long) redisTemplate.execute(new DefaultRedisScript(unlockScript,Long.class),Arrays.asList(lockKey),uuidValue);
        if (flag == null) {
            throw new RuntimeException("this lock doesn't exist");
        }
    }

    //尝试加锁
    @Override
    public boolean tryLock() {
        try {
            tryLock(-1L,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    //尝试加锁
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (time == -1){
            log.info("trylock: lockKey:{},uuidValue:{}", lockKey, uuidValue);
            //加锁lua脚本
            String lockScript = "if redis.call('exists',KEYS[1]) == 0 or redis.call('hexists',KEYS[1],KEYS[2]) == 1 then " +
                    "redis.call('hincrby',KEYS[1],ARGV[1],1) " +
                    "redis.call('expire',KEYS[1],ARGV[2]) " +
                    "return 1 " +
                    "else " +
                    "return 0 " +
                    "end";
            while(!redisTemplate.execute(new DefaultRedisScript(lockScript,Long.class), Arrays.asList(lockKey), uuidValue, String.valueOf(expireTime)).equals(1L)){
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            //添加自动续期功能
            renewExpire();
            return true;
        }
        return false;
    }

    private void renewExpire() {
        String script = "if redis.call('hexists',KEYS[1],ARGV[1]) == 1 then " +
                "return redis.call('expire',KEYS[1],ARGV[2]) " +
                "else return 0 end";
        //每十秒判断是否需要续期
        timer.schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                if (redisTemplate.execute(new DefaultRedisScript(script,Long.class),Arrays.asList(lockKey),uuidValue,String.valueOf(expireTime)).equals(1L)){
                    //不断进行续期
                    renewExpire();
                }else{
                    timer.purge();
                }
            }
        },(this.expireTime * 1000)/3);
    }

    //考虑把锁中断
    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
    //锁配条件变量
    @Override
    public Condition newCondition() {
        return null;
    }
}
