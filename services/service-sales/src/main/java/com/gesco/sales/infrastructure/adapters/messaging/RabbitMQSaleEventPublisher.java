package com.gesco.sales.infrastructure.adapters.messaging;

import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleEventPublisher;
import com.gesco.sales.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RabbitMQSaleEventPublisher implements SaleEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishSaleCreated(Sale sale) {
        SaleCreatedEvent event = SaleCreatedEvent.builder()
                .orderId(sale.getId().toString())
                .customerId(sale.getCustomerId())
                .totalAmount(sale.getTotalAmount())
                .items(sale.getItems().stream()
                        .map(item -> SaleCreatedEvent.ItemEvent.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SALES_EXCHANGE,
                RabbitMQConfig.SALE_CREATED_ROUTING_KEY,
                event);
    }
}
