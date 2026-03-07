package com.gesco.sales.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SALES_EXCHANGE = "sales.exchange";
    public static final String SALE_CREATED_ROUTING_KEY = "sale.created";
    public static final String NOTIFICATION_QUEUE = "notification_queue";

    @Bean
    public TopicExchange salesExchange() {
        return new TopicExchange(SALES_EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange salesExchange) {
        return BindingBuilder.bind(notificationQueue).to(salesExchange).with(SALE_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
