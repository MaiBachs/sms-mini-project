package com.consumer.sms.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.consumer.sms.util.Constant;

import java.util.Properties;

public class RabbitMQConfig {
    private Connection connection;
    private String exchange;
    private String routingKey;

    public ConnectionFactory init(Properties props) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(props.getProperty(Constant.Property.RABBIT_HOST));
        factory.setUsername(props.getProperty(Constant.Property.RABBIT_USERNAME));
        factory.setPassword(props.getProperty(Constant.Property.RABBIT_PASSWORD));
        factory.setAutomaticRecoveryEnabled(true);
        factory.setRequestedHeartbeat(30);
        exchange = props.getProperty(Constant.Property.RABBIT_EXCHANGE);
        routingKey = props.getProperty(Constant.Property.RABBIT_ROUTING_KEY);
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