package com.consumer.sms.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.consumer.sms.util.Constant;
import java.sql.SQLException;
import java.util.Properties;

public class RedisPool {
    private JedisPool jedisPool;

    public void initialize(Properties props) {
        jedisPool = new JedisPool(new JedisPoolConfig(), props.getProperty(Constant.Property.REDIS_HOST)
                , Integer.parseInt(props.getProperty(Constant.Property.REDIS_PORT)));
    }
    public Jedis getConnection() throws SQLException {
        return jedisPool.getResource();
    }
    public void close() {
        jedisPool.close();
    }
}
