package com.consumer.sms.service.impl;

import com.consumer.sms.service.RedisService;
import redis.clients.jedis.Jedis;
import java.time.Instant;

public class RedisServiceImpl implements RedisService {
    private Jedis jedis;
    private int maxTps;

    public RedisServiceImpl(Jedis jedis, int maxTps) {
        this.jedis = jedis;
        this.maxTps = maxTps;
    }

    @Override
    public void setStr(String key, String value) {
        jedis.set(key, value);
    }

    @Override
    public String getStr(String key) {
        return jedis.get(key);
    }

    @Override
    public boolean checkExist(String key, String value) {
        String result = jedis.get(key);
        return value != null && value.equals(result);
    }

    @Override
    public boolean allowSendRequest() {
        long second = Instant.now().getEpochSecond();
        String key = "sms:tps:" + second;
        long count = jedis.incr(key);
        if (count == 1) {
            jedis.expire(key, 2);
        }
        return count <= maxTps;
    }
}
