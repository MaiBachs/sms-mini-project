package com.consumer.sms.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQDelayConfig {
    @Value("${app.rabbitmq.queue}")
    private String mainQueue;
    @Value("${app.rabbitmq.queue.delay}")
    private String delayQueue;

    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        // Delay 1s
        args.put("x-message-ttl", 1000);

        // push to main queue
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", mainQueue);

        return new Queue(
                delayQueue,
                true,
                false,
                false,
                args
        );
    }
}
