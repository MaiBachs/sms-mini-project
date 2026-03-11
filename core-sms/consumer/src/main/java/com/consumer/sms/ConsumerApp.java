package com.consumer.sms;

import com.consumer.sms.config.DbPool;
import com.consumer.sms.config.RabbitMQConfig;
import com.consumer.sms.config.RedisPool;
import com.consumer.sms.consumer.SmsConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.Properties;

public class ConsumerApp {
    private static final Logger log = LoggerFactory.getLogger(ConsumerApp.class);

    public static void main(String[] args) {
        log.info("Starting consumer");

        try {
            Properties props = new Properties();
            InputStream in = ConsumerApp.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(in);
            System.getProperties().putAll(props);
            // init connection pool
            DbPool.initialize(props);
            RedisPool redisPool = new RedisPool();
            redisPool.initialize(props);
            RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
            rabbitMQConfig.init(props);
            // consum message
            SmsConsumer smsConsumer = new SmsConsumer(rabbitMQConfig.getConnection(), props, redisPool.getConnection());
            smsConsumer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

