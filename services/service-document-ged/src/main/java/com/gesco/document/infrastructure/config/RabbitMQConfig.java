package com.gesco.document.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SALES_EXCHANGE = "sales.exchange";
    public static final String DOCUMENT_QUEUE = "document.sale.created.queue";
    public static final String SALE_CREATED_ROUTING_KEY = "sale.created";

    @Bean
    public TopicExchange salesExchange() {
        return new TopicExchange(SALES_EXCHANGE);
    }

    @Bean
    public Queue documentQueue() {
        return QueueBuilder.durable(DOCUMENT_QUEUE).build();
    }

    @Bean
    public Binding documentBinding(Queue documentQueue, TopicExchange salesExchange) {
        return BindingBuilder.bind(documentQueue).to(salesExchange).with(SALE_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
