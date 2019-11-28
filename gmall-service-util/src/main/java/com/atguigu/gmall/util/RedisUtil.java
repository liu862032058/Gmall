package com.atguigu.gmall.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

//    单例
    private JedisPool jedisPool;
//    初始化
    public void initPool(String host,int port,int database){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(200); //最大连接数
        poolConfig.setMaxIdle(30);//最大空闲连接数

        //当资源池用尽后，调用者是否要等待。只有当值为true时，下面的maxWaitMillis才会生效。
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(10*1000);//当资源池连接用尽后，调用者的最大等待时间
        poolConfig.setTestOnBorrow(true);//向资源池借用连接时是否做连接有效性检测
        jedisPool=new JedisPool(poolConfig,host,port,20*1000);
    }

    public Jedis getJedis(){
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

}
