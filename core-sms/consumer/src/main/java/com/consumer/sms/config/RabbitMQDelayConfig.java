package com.consumer.sms.config;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RabbitMQDelayConfig {
    private final String mainQueue;
    private final String delayQueue;

    public RabbitMQDelayConfig(String mainQueue, String delayQueue) {
        this.mainQueue = mainQueue;
        this.delayQueue = delayQueue;
    }

    public void declareQueues(Channel channel) throws IOException {
        channel.queueDeclare(
                mainQueue,
                true,
                false,
                false,
                null
        );

        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 1000);
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", mainQueue);
        channel.queueDeclare(
                delayQueue,
                true,
                false,
                false,
                args
        );
    }
}
