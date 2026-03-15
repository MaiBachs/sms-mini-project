package com.consumer.sms.consumer;

import com.consumer.sms.dto.SmsRequest;
import com.consumer.sms.dto.SmsResponse;
import com.consumer.sms.service.RedisService;
import com.consumer.sms.service.SmsService;
import com.consumer.sms.service.impl.RedisServiceImpl;
import com.consumer.sms.service.impl.SmsServiceImpl;
import com.consumer.sms.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.*;

public class SmsConsumer {
    private static final Logger log = LoggerFactory.getLogger(SmsConsumer.class);
    private final String queueName;
    private final String delayQueueName;
    private final Connection connection;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String secretKey;
    private RedisService redisService;
    private SmsService smsService = new SmsServiceImpl();
    private ExecutorService executor;
    private long timeRetry;

    public SmsConsumer(Connection rabbitConnection, Properties props, Jedis jedis) {
        this.queueName = props.getProperty(Constant.Property.RABBIT_QUEUE);
        this.delayQueueName = props.getProperty(Constant.Property.RABBIT_DELAY_QUEUE);
        this.secretKey = props.getProperty(Constant.Property.SECRET_KEY);
        this.connection = rabbitConnection;
        this.redisService = new RedisServiceImpl(jedis, Integer.parseInt(props.getProperty(Constant.Property.SMS_TPS)));
        this.executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty(Constant.Property.SCAN_THREAD)));
        this.timeRetry = Long.parseLong(props.getProperty(Constant.Property.SMS_RETRY_DELAY));
    }

    public void start() throws Exception {
        executor.submit(() -> {
            try {
                Channel channel = connection.createChannel();
                channel.basicConsume(
                        queueName,
                        false,
                        (consumerTag, delivery) -> handleMessage(consumerTag, channel, delivery),
                        consumerTag -> log.warn("Consumer cancelled: {}", consumerTag)
                );
            } catch (Exception e) {
                log.error("Error creating consumer", e);
            }
        });
    }

    private void handleMessage(String consumerTag, Channel channel, Delivery delivery) {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        long tag = delivery.getEnvelope().getDeliveryTag();
        try {
            log.info("Received message: {}", message);
            SmsRequest sms = objectMapper.readValue(message, SmsRequest.class);
            if (!redisService.allowSendRequest()) {
                // push to delayQueue if max tps
                log.warn("=== Max tps, push to queue delay with messageId {} ===", sms.getMessageId());
                channel.basicPublish("", delayQueueName, null, message.getBytes());
                channel.basicAck(tag, false);
                return;
            }
            // Call API gateway
            sms.setEncryptMessage(Constant.SmsData.ENCRYPT_MESSAGE_DEFAULT);
            sms.setIsEncrypt(Constant.SmsData.IS_ENCRYPT_DEFAULT);
            sms.setType(Constant.SmsData.TYPE_DEFAULT);
            sms.setRequestTime(String.valueOf(new java.util.Date().getTime()));
            sms.setSercretKey(secretKey);
            // check duplicate sms
            boolean checkSmsDuplicate = redisService.checkExist(sms.getMessageId(), sms.getShortMessage());
            if (!checkSmsDuplicate) {
                // send sms
                SmsResponse response = smsService.sendWithRetryAndUpdateStatusSms(sms, timeRetry);
                redisService.setStr(sms.getMessageId(), sms.getShortMessage());
                log.info("Message {}, Response call gateway: {}", sms, response);
            } else {
                log.error("Sms duplicate");
            }

            // cf remove message
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
