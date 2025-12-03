package com.zejas.authsvr;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.zejas.authsvr.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SsmApplicationTests {

    @Autowired
    UserService userService;

    @DisplayName("测试1")
    @Test
    void contextLoads() {
//        Assertions.assertAll(
//                "person",
//                () -> Assertions.assertEquals(),
//                () -> Assertions.assertEquals()
//        );
//        Assertions.assertThrows()
    }

    @Autowired
    StringRedisTemplate redisTemplate;

    @DisplayName("redisTemplate")
    @Test
    void test(){
        redisTemplate.opsForValue().set("string","ssm");
        redisTemplate.opsForList().leftPush("list","1");
        redisTemplate.opsForList().leftPush("list","2");
        String result = redisTemplate.opsForList().leftPop("list");
        assertEquals("1",result);
        redisTemplate.opsForSet().add("set","s","a","s");
        assertTrue(redisTemplate.opsForSet().isMember("222", "s"));
        redisTemplate.opsForZSet().add("zset","张三",99);
        redisTemplate.opsForZSet().add("zset","里斯",100);
        ZSetOperations.TypedTuple<String> popMax =  redisTemplate.opsForZSet().popMax("111");
        popMax.getScore();
        popMax.getValue();
        redisTemplate.opsForHash().put("hash","ssm","1");
        redisTemplate.opsForHash().get("hash","ssm");
    }

    @DisplayName("bloomFilter")
    @Test
    public void guavaTestWithBloomFilter(){
        //创建布隆过滤器
        BloomFilter<Long> longBloomFilter = BloomFilter.create(Funnels.longFunnel(),100);
        //判断指定的元素是否存在
        System.out.println(longBloomFilter.mightContain(1L));
        System.out.println(longBloomFilter.mightContain(2L));
        longBloomFilter.put(1L);
        System.out.println(longBloomFilter.mightContain(1L));
    }

}
