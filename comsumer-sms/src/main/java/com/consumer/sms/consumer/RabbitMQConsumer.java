package com.consumer.sms.consumer;

import com.consumer.sms.dto.request.SmsRequest;
import com.consumer.sms.dto.response.SmsResponse;
import com.consumer.sms.service.RedisService;
import com.consumer.sms.service.SmsService;
import com.consumer.sms.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {
    @Value("${secret.key}")
    private String secretKey;
    @Value("${app.rabbitmq.queue.delay}")
    private String delayQueue;
    private final ObjectMapper objectMapper;
    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
        try {
            SmsRequest sms = objectMapper.readValue(message, SmsRequest.class);
            // check tps sms
            if (!redisService.allowSendRequest()) {
                // push to delayQueue if max tps
                log.warn("=== Max tps, push to queue delay with messageId {} ===", sms.getMessageId());
                rabbitTemplate.convertAndSend(delayQueue, message);
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
                log.info("Sms duplicate");
            }

        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
