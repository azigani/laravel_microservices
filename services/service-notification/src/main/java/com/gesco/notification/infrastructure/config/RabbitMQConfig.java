package com.gesco.notification.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_QUEUE = "notification_queue";
    public static final String SALES_EXCHANGE = "sales.exchange";

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, false);
    }

    @Bean
    public Binding binding(Queue notificationQueue) {
        return BindingBuilder.bind(notificationQueue)
                .to(new TopicExchange(SALES_EXCHANGE))
                .with("sale.created");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
