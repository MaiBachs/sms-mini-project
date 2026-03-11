package com.produce.sms;

import com.produce.sms.config.RabbitMQConfig;
import com.produce.sms.schedule.ScheduleScanSms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ProducerApplication {
    private static final Logger log = LoggerFactory.getLogger(ProducerApplication.class);

    public static void main(String[] args) throws Exception {
        log.info("Starting producer");
        Properties props = new Properties();
        try (InputStream in = ProducerApplication.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(in);
        } catch (Exception e) {
            log.info("=== Get property error ===");
        }
        System.getProperties().putAll(props);
        // init queue
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
        rabbitMQConfig.init(props);
        // call schedule
        ScheduleScanSms scanner = new ScheduleScanSms(props, rabbitMQConfig.getConnection());
        scanner.start();
    }
}
