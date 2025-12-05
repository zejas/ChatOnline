package com.zejas.authsvr;

import com.zejas.authsvr.util.DistributedLockFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/31 17:24
 */
@Slf4j
@SpringBootTest
public class NIOTests {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    DistributedLockFactory distributedLockFactory;

//    @Autowired
//    Redisson redisson;

    @Test
    public void test() throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8011));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");
                }else if(key.isReadable()){
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(6);
                    int len = client.read(buffer);
                    if (len > 0) {
                        System.out.println("输入数据：" + new String(buffer.array()));
                    }else if(len == -1){
                        System.out.println("客户端断开连接");
                        client.close();
                    }
                }
                //删除本次的key
                iterator.remove();
            }


        }

    }

    String redisOldkey_A = "111";
    String redisOldkey_B = "222";
    String redisNewkey = "333";

    @Test
    public void test2() {
        redisTemplate.delete(redisOldkey_B);
        redisTemplate.opsForList().leftPushAll(redisOldkey_B, List.of(new String[]{"1", "13"}));
        redisTemplate.expire(redisOldkey_B, Duration.ofDays(1L).plusSeconds(10L));

        redisTemplate.delete(redisOldkey_A);
        redisTemplate.opsForList().leftPushAll(redisOldkey_A, List.of(new String[]{"1", "13"}));
        redisTemplate.expire(redisOldkey_A, Duration.ofDays(1L));


    }
    @Test
    public void test3() {
        int page = 3,size = 5;
        int start = (page-1)*size;
        int end = start + size - 1;
        List<String> list = null;
        list = (List<String>) redisTemplate.opsForList().range(redisOldkey_A,start,end);
        if (CollectionUtils.isEmpty(list)){
            log.info("A缓存过期失效了");
            list = (List<String>) redisTemplate.opsForList().range(redisOldkey_B,start,end);
            if (CollectionUtils.isEmpty(list)){
                //走mysql
            }
        }
    }
    @Test
    public void test4() {
        String key = "redislock";
        String value = UUID.randomUUID().toString() + Thread.currentThread().getId();
        while(!redisTemplate.opsForValue().setIfAbsent(key, value, 10, TimeUnit.SECONDS)){
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            //业务处理
        } finally {
            //修改为lua脚本
            String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del',KEYS[1]) " +
                    "else " +
                    "return 0 " +
                    "end";
            redisTemplate.execute(new DefaultRedisScript(luaScript, Long.class),List.of(key),value);
        }

    }
    @Test
    public void test5() throws InterruptedException {
        Lock lock = distributedLockFactory.getDistributedLock("redis");
        lock.lock();
        TimeUnit.SECONDS.sleep(30);
        //业务处理

    }
//    @Test
//    public void test6() {
//        RLock lock = redisson.getLock("redisLock");
//        lock.lock(30,TimeUnit.SECONDS);
//        try {
//            TimeUnit.SECONDS.sleep(30);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        if(lock.isLocked() && lock.isHeldByCurrentThread()){
//            lock.unlock();
//        }
//
//    }

}
