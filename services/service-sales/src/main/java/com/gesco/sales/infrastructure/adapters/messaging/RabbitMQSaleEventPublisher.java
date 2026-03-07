package com.gesco.sales.infrastructure.adapters.messaging;

import com.gesco.sales.core.application.mapper.SaleMapper;
import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleEventPublisher;
import com.gesco.sales.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQSaleEventPublisher implements SaleEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final SaleMapper saleMapper;

    @Override
    public void publishSaleCreated(Sale sale) {
        SaleCreatedEventDto eventDto = saleMapper.toEventDto(sale);
        rabbitTemplate.convertAndSend(RabbitMQConfig.SALES_EXCHANGE, "sale.created", eventDto);
    }
}