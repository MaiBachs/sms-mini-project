package com.consumer.sms.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.consumer.sms.util.Constant;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RabbitMQConfig {
    private Connection connection;
    private String exchange;
    private String routingKey;

    private BlockingQueue<Channel> channelPool;
    private int poolSize = 10;

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
        initChannelPool();
        return factory;
    }

    private void initChannelPool() throws Exception {
        channelPool = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            Channel channel = connection.createChannel();
            channelPool.offer(channel);
        }
    }

    public Channel borrowChannel() throws InterruptedException {
        return channelPool.take();
    }

    public void returnChannel(Channel channel) {
        if (channel != null) {
            channelPool.offer(channel);
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