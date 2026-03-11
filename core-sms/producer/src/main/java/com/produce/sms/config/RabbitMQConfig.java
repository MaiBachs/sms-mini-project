package com.produce.sms.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Properties;

public class RabbitMQConfig {
    private Connection connection;
    private String exchange;
    private String queue;
    private String routingKey;

    public void init(Properties props) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(props.getProperty("rabbit.host"));
        factory.setUsername(props.getProperty("rabbit.username"));
        factory.setPassword(props.getProperty("rabbit.password"));
        factory.setAutomaticRecoveryEnabled(true);
        factory.setRequestedHeartbeat(30);
        exchange = props.getProperty("rabbit.exchange");
        queue = props.getProperty("rabbit.queue");
        routingKey = props.getProperty("rabbit.routingKey");
        connection = factory.newConnection();
        initTopology();
    }

    private void initTopology() throws Exception {
        try (Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchange, "direct", true);
            channel.queueDeclare(queue, true, false, false, null);
            channel.queueBind(queue, exchange, routingKey);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}