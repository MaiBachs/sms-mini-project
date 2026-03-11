package com.consumer.sms.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.sql.SQLException;
import java.util.Properties;

public class RedisPool {
    private JedisPool jedisPool;

    public void initialize(Properties props) {
        jedisPool = new JedisPool(new JedisPoolConfig(), props.getProperty("redis.host")
                , Integer.parseInt(props.getProperty("redis.port")));
    }
    public Jedis getConnection() throws SQLException {
        return jedisPool.getResource();
    }
    public void close() {
        jedisPool.close();
    }
}
