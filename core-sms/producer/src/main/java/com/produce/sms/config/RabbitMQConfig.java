package com.produce.sms.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.produce.sms.util.Constant;
import java.util.Properties;

public class RabbitMQConfig {
    private Connection connection;
    private String exchange;
    private String queue;
    private String routingKey;

    public void init(Properties props) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(props.getProperty(Constant.Property.RABBIT_HOST));
        factory.setUsername(props.getProperty(Constant.Property.RABBIT_USERNAME));
        factory.setPassword(props.getProperty(Constant.Property.RABBIT_PASSWORD));
        factory.setAutomaticRecoveryEnabled(true);
        factory.setRequestedHeartbeat(30);
        exchange = props.getProperty(Constant.Property.RABBIT_EXCHANGE);
        queue = props.getProperty(Constant.Property.RABBIT_QUEUE);
        routingKey = props.getProperty(Constant.Property.RABBIT_ROUTING_KEY);
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