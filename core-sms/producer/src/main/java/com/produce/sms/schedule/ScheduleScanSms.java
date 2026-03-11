package com.produce.sms.schedule;

import com.produce.sms.entity.SmsTest;
import com.produce.sms.service.SmsService;
import com.produce.sms.service.impl.SmsServiceImpl;
import com.produce.sms.config.DbPool;
import com.produce.sms.util.Constant;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class ScheduleScanSms {
    private static final Logger log = LoggerFactory.getLogger(ScheduleScanSms.class);
    private final SmsService smsService = new SmsServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    private String exchange;
    private String routingKey;
    private long scanInterval;
    private ExecutorService executor;
    private Channel channel;

    public ScheduleScanSms(Properties props, Connection rabbitConnection) throws IOException {
        exchange = props.getProperty(Constant.Property.RABBIT_EXCHANGE);
        routingKey = props.getProperty(Constant.Property.RABBIT_ROUTING_KEY);
        scanInterval = Long.parseLong(props.getProperty(Constant.Property.SCAN_INTERVAL_MS));
        executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty(Constant.Property.SCAN_THREAD)));
        channel = rabbitConnection.createChannel();
    }

    public void start() {
        log.info("initializing database pool and scheduler");
        DbPool.initialize();
        ScheduledExecutorService sched = Executors.newSingleThreadScheduledExecutor();
        sched.scheduleAtFixedRate(this::scan, 0, scanInterval, TimeUnit.MILLISECONDS);
    }

    private void scan() {
        log.info("=== Start scan send sms to rabbitmq time: {} ===", new Date());
        executor.submit(() -> processSms());
    }

    private void processSms() {
        List<SmsTest> smsList = smsService.getListSmsNoReadAndClaim();
        log.info("Sms size: {}", smsList.size());
        if (smsList.isEmpty()) {
            return;
        }
        for (SmsTest sms : smsList) {
            try {
                log.info("Thread {} processing SMS {}", Thread.currentThread().getName(), sms.getMessageId());
                String json = mapper.writeValueAsString(sms);
                channel.basicPublish(exchange, routingKey, null, json.getBytes());
                log.info("Sent SMS id: {}", sms.getMessageId());
            } catch (Exception e) {
                log.error("Error processing SMS id: {}", sms.getMessageId(), e);
            }
        }
    }
}
