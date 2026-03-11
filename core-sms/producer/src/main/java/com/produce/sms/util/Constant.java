package com.produce.sms.util;

public class Constant {
    public static class SmsStatus {
        public static final String NONE = "NONE";
        public static final String PROCESSING = "PROCESSING";
        public static final String COMPLETE = "COMPLETE";
        public static final String FAILED = "FAILED";
    }

    public static class Property {
        public static final String RABBIT_QUEUE = "rabbit.queue";
        public static final String RABBIT_EXCHANGE = "rabbit.exchange";
        public static final String RABBIT_ROUTING_KEY = "rabbit.routingKey";
        public static final String RABBIT_HOST = "rabbit.host";
        public static final String RABBIT_PORT = "rabbit.port";
        public static final String RABBIT_USERNAME = "rabbit.username";
        public static final String RABBIT_PASSWORD = "rabbit.password";
        public static final String DB_URL = "db.url";
        public static final String DB_USERNAME = "db.username";
        public static final String DB_PASSWORD = "db.password";
        // other property keys as needed
    }
}
