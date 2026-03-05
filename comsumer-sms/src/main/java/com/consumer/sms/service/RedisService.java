package com.consumer.sms.service;

public interface RedisService {
    void setStr(String key, String value);
    String getStr(String key);
    boolean checkExist(String key, String value);
    boolean allowSendRequest();
}
