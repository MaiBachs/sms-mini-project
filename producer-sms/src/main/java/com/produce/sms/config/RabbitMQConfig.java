package com.produce.sms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${app.rabbitmq.exchange}")
    private String exchange;
    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;
    @Value("${app.rabbitmq.queue}")
    private String queue;
    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;
    @Value("${spring.rabbitmq.username}")
    private String rabbitUserName;
    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Bean
    public Queue queue()
    {
        return new Queue(queue, false);
    }

    @Bean public Exchange exchange()
    {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange)
    {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKey)
                .noargs();
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory =
                new CachingConnectionFactory(rabbitHost);

        factory.setUsername(rabbitUserName);
        factory.setPassword(rabbitPassword);
        factory.setChannelCacheSize(50);
        factory.setChannelCheckoutTimeout(1000);
        return factory;
    }
}
