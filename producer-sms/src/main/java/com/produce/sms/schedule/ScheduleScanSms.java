package com.produce.sms.schedule;

import com.produce.sms.entity.SmsTest;
import com.produce.sms.repository.SmsRepository;
import com.produce.sms.service.SmsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

@Component
@Slf4j
public class ScheduleScanSms {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SmsRepository smsRepository;
    @Value("${app.rabbitmq.exchange}")
    private String exchange;
    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;
    @Autowired
    private Executor smsExecutor;

    @Scheduled(fixedRateString = "${sms.time.scan}")
    @Transactional
    public void scanSmsToSentRabbit() {
        log.info("=== Start scan send sms to rabbitmq time: {} ===", new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        smsExecutor.execute(() -> {
            log.info("Thread {} is processing",Thread.currentThread().getName());
            List<SmsTest> smsList = smsService.getListSmsNoReadAndClaim();
            log.info("Sms size: {}", smsList.size());
            if (smsList.isEmpty()) {
                return;
            }
            for (SmsTest sms : smsList) {
                try {
                    String json = objectMapper.writeValueAsString(sms);
                    rabbitTemplate.convertAndSend(exchange, routingKey, json);
                    log.info("Sent SMS id: {}", sms.getMessageId());
                } catch (Exception e) {
                    log.error("Error sending SMS id: {}", sms.getMessageId(), e);
                }
            }
        });
    }
}
