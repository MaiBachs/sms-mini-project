package com.consumer.sms.service.impl;

import com.consumer.sms.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final StringRedisTemplate redisTemplate;
    @Value("${sms.tps}")
    private Integer maxTps;

    @Override
    public void setStr(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getStr(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean checkExist(String key, String value) {
        String shortMessage = redisTemplate.opsForValue().get(key);
        if (Objects.equals(shortMessage, value)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean allowSendRequest() {
        long currentSecond = System.currentTimeMillis() / 1000;
        String key = "sms:tps:" + currentSecond;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == null) {
            return false;
        }
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(2));
        }
        if (count > maxTps) {
            return false;
        }
        return true;
    }
}
