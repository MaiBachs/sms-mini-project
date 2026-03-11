package com.consumer.sms.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Properties;

public class RabbitMQConfig {
    private Connection connection;
    private String exchange;
    private String routingKey;

    public ConnectionFactory init(Properties props) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(props.getProperty("rabbit.host"));
        factory.setUsername(props.getProperty("rabbit.username"));
        factory.setPassword(props.getProperty("rabbit.password"));
        factory.setAutomaticRecoveryEnabled(true);
        factory.setRequestedHeartbeat(30);
        exchange = props.getProperty("rabbit.exchange");
        routingKey = props.getProperty("rabbit.routingKey");
        connection = factory.newConnection();
        return factory;
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