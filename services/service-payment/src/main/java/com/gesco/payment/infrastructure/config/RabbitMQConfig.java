package com.gesco.payment.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${payment.rabbitmq.sales-exchange}")
    private String salesExchangeName;

    @Value("${payment.rabbitmq.payment-exchange}")
    private String paymentExchangeName;

    @Value("${payment.rabbitmq.invoice-queue}")
    private String invoiceQueueName;

    @Value("${payment.rabbitmq.sales-routing-key}")
    private String salesRoutingKey;

    @Bean
    public TopicExchange salesExchange() {
        return new TopicExchange(salesExchangeName);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(paymentExchangeName);
    }

    @Bean
    public Queue invoiceQueue() {
        return new Queue(invoiceQueueName, true);
    }

    @Bean
    public Binding invoiceBinding(Queue invoiceQueue, TopicExchange salesExchange) {
        return BindingBuilder.bind(invoiceQueue).to(salesExchange).with(salesRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
