package com.produce.sms.schedule;

import com.produce.sms.entity.SmsTest;
import com.produce.sms.repository.SmsRepository;
import com.produce.sms.service.SmsService;
import com.produce.sms.util.Constant;
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
        List<SmsTest> smsTestList = smsService.getListSmsNoRead();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Sms size: {}", smsTestList.size());
        for (SmsTest sms : smsTestList) {
            smsExecutor.execute(() -> {
                try {
                    // log thread
                    String threadName = Thread.currentThread().getName();
                    log.info("Thread {} is processing SMS {}",
                            threadName,
                            sms.getMessageId());
                    // update status to processing
                    int updated = smsRepository.claimSms(sms.getMessageId(), Constant.SmsStatus.NONE, Constant.SmsStatus.PROCESSING);
                    if (updated == 1) {
                        String json = objectMapper.writeValueAsString(sms);
                        rabbitTemplate.convertAndSend(exchange, routingKey, json);
                        log.info("Sent SMS id: {}", sms.getMessageId());
                    } else {
                        log.debug("SMS {} already claimed", sms.getMessageId());
                    }
                } catch (Exception e) {
                    log.error("Error sending SMS id: {}", sms.getMessageId(), e);
                }
            });
        }
    }
}
