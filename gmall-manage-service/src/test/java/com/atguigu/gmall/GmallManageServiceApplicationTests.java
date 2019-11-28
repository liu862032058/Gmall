package com.atguigu.gmall;

import com.atguigu.gmall.util.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
public class GmallManageServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;
    @Test
   public void contextLoads() {
        Jedis jedis = redisUtil.getJedis();
        String a = jedis.set("a","123");
        String a1 = jedis.get("a");
        System.out.println(a1);

    }

}
