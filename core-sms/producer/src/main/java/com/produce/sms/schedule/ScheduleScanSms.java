package com.produce.sms.schedule;

import com.produce.sms.entity.SmsTest;
import com.produce.sms.repository.SmsRepository;
import com.produce.sms.service.SmsService;
import com.produce.sms.service.impl.SmsServiceImpl;
import com.produce.sms.util.Constant;
import com.produce.sms.util.DbPool;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class ScheduleScanSms {
    private static final Logger log = LogManager.getLogger(ScheduleScanSms.class);
    private final SmsService smsService = new SmsServiceImpl();
    private final SmsRepository smsRepository = new SmsRepository();
    private final ObjectMapper mapper = new ObjectMapper();
    private String exchange;
    private String routingKey;
    private ConnectionFactory factory;
    private long scanInterval;
    private ExecutorService executor;
    private Connection conn;
    private Channel channel;

    public ScheduleScanSms(Properties props) throws IOException, TimeoutException {
        exchange = props.getProperty("rabbit.exchange");
        routingKey = props.getProperty("rabbit.routingKey");
        scanInterval = Long.parseLong(props.getProperty("scan.interval.ms"));
        factory = new ConnectionFactory();
        factory.setHost(props.getProperty("rabbit.host"));
        factory.setPort(Integer.parseInt(props.getProperty("rabbit.port")));
        factory.setUsername(props.getProperty("rabbit.username"));
        factory.setPassword(props.getProperty("rabbit.password"));
        executor = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("scan.thread")));
        conn = factory.newConnection();
        channel = conn.createChannel();
    }

    public void start() {
        log.info("initializing database pool and scheduler");
        DbPool.initialize();
        ScheduledExecutorService sched = Executors.newSingleThreadScheduledExecutor();
        sched.scheduleAtFixedRate(this::scan, 0, scanInterval, TimeUnit.MILLISECONDS);
    }

    private void scan() {
        log.info("=== Start scan send sms to rabbitmq time: {} ===", new Date());
        List<SmsTest> smsList = smsService.getListSmsNoRead();
        if (smsList == null || smsList.isEmpty()) {
            return;
        }
        log.info("Sms size: {}", smsList.size());

        for (SmsTest sms : smsList) {
            executor.submit(() -> processSms(sms));
        }
    }

    private void processSms(SmsTest sms) {
        String thread = Thread.currentThread().getName();
        try {
            log.info("Thread {} processing SMS {}", thread, sms.getMessageId());
            int updated = smsRepository.claimSms(
                    sms.getMessageId(),
                    Constant.SmsStatus.NONE,
                    Constant.SmsStatus.PROCESSING
            );

            if (updated != 1) {
                log.debug("SMS {} already claimed", sms.getMessageId());
                return;
            }
            String json = mapper.writeValueAsString(sms);
            channel.basicPublish(exchange, routingKey, null, json.getBytes());
            log.info("Sent SMS id: {}", sms.getMessageId());
        } catch (Exception e) {
            log.error("Error processing SMS id: {}", sms.getMessageId(), e);
        }
    }
}
