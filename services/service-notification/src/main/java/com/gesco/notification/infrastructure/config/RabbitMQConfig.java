package com.gesco.notification.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_QUEUE = "notification_queue";
    public static final String PAYMENT_COMPLETED_NOTIFICATION_QUEUE = "notification_payment_completed_queue";
    public static final String SALES_EXCHANGE = "sales.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, false);
    }

    @Bean
    public Queue paymentCompletedNotificationQueue() {
        return new Queue(PAYMENT_COMPLETED_NOTIFICATION_QUEUE, false);
    }

    @Bean
    public TopicExchange salesExchange() {
        return new TopicExchange(SALES_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange salesExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(salesExchange)
                .with("sale.created");
    }

    @Bean
    public Binding paymentCompletedBinding(Queue paymentCompletedNotificationQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentCompletedNotificationQueue)
                .to(paymentExchange)
                .with("payment.completed");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
