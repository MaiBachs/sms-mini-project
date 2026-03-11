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

public class SmsConsumer {
    private static final Logger log = LoggerFactory.getLogger(SmsConsumer.class);
    private final String queueName;
    private final String delayQueueName;
    private final Connection connection;
    private Channel channel;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String secretKey;
    private RedisService redisService;
    private SmsService smsService = new SmsServiceImpl();

    public SmsConsumer(Connection rabbitConnection, Properties props, Jedis jedis) {
        this.queueName = props.getProperty("rabbit.queue");
        this.delayQueueName = props.getProperty("rabbit.delay.queue");
        this.secretKey = props.getProperty("secret.key");
        this.connection = rabbitConnection;
        this.redisService = new RedisServiceImpl(jedis, Integer.parseInt(props.getProperty("sms.tps")));
    }

    public void start() throws Exception {
        channel = connection.createChannel();
        channel.basicConsume(
                queueName,
                false,
                this::handleMessage,
                consumerTag -> log.warn("Consumer cancelled: {}", consumerTag)
        );
    }

    private void handleMessage(String consumerTag, Delivery delivery) {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        long tag = delivery.getEnvelope().getDeliveryTag();
        try {
            log.info("Received message: {}", message);
            SmsRequest sms = objectMapper.readValue(message, SmsRequest.class);
            if (!redisService.allowSendRequest()) {
                // push to delayQueue if max tps
                log.warn("=== Max tps, push to queue delay with messageId {} ===", sms.getMessageId());
                channel.basicPublish("", delayQueueName, null, message.getBytes());
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
                SmsResponse response = smsService.sendWithRetryAndUpdateStatusSms(sms);
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
