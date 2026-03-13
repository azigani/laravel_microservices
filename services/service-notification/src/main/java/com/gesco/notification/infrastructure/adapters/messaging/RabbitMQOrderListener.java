package com.gesco.notification.infrastructure.adapters.messaging;

import com.gesco.notification.core.application.dto.OrderPlacedEventDto;
import com.gesco.notification.core.application.usecase.ProcessOrderNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQOrderListener {
    private final ProcessOrderNotificationUseCase processOrderNotificationUseCase;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleSaleCreated(SaleCreatedEventDto event) {
        log.info("[Notification Service] Received sale.created event for Sale ID: {}", event.getSaleId());
        processOrderNotificationUseCase.execute(event);
    }
}
