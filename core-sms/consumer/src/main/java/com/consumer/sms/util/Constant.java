package com.consumer.sms.util;

public class Constant {
    public static class SmsStatus {
        public static final String NONE = "NONE";
        public static final String COMPLETE = "COMPLETE";
        public static final String FAILED = "FAILED";
        public static final String PROCESSING = "PROCESSING";
    }

    public static class SmsData {
        public static final String ENCRYPT_MESSAGE_DEFAULT = "";
        public static final Integer IS_ENCRYPT_DEFAULT = 0;
        public static final Integer TYPE_DEFAULT = 0;
    }

    public static class GatewayStatus {
        public static final String STATUS_SUCCESS = "200";
    }

    public static class Property {
        public static final String RABBIT_QUEUE = "rabbit.queue";
        public static final String RABBIT_DELAY_QUEUE = "rabbit.delay.queue";
        public static final String RABBIT_EXCHANGE = "rabbit.exchange";
        public static final String RABBIT_ROUTING_KEY = "rabbit.routingKey";
        public static final String RABBIT_HOST = "rabbit.host";
        public static final String RABBIT_USERNAME = "rabbit.username";
        public static final String RABBIT_PASSWORD = "rabbit.password";
        public static final String REDIS_HOST = "redis.host";
        public static final String REDIS_PORT = "redis.port";
        public static final String DB_URL = "db.url";
        public static final String DB_USERNAME = "db.username";
        public static final String DB_PASSWORD = "db.password";
        public static final String POOL_MAXIMUM_POOL_SIZE = "pool.maximumPoolSize";
        public static final String SECRET_KEY = "secret.key";
        public static final String SMS_TPS = "sms.tps";
        // other property keys can be added here
    }
}
