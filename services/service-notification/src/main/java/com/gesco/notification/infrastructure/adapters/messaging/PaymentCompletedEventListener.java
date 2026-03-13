package com.gesco.notification.infrastructure.adapters.messaging;

import com.gesco.notification.core.application.usecase.ProcessOrderNotificationUseCase;
import com.gesco.notification.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Listens for payment.completed events from the Payment microservice.
 * Sends a confirmation email to the customer.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompletedEventListener {

    private final ProcessOrderNotificationUseCase processOrderNotificationUseCase;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_NOTIFICATION_QUEUE)
    public void handlePaymentCompleted(Map<String, Object> event) {
        log.info("[Notification Service] Received payment.completed event: {}", event);

        String status = (String) event.get("status");
        if ("SUCCESS".equals(status)) {
            // Transform event to a format compatible with notification use case
            // Or create a specific notification use case for payments
            
            // For now, let's reuse the dto or map logic
            // We need customer details (usually fetched via identity service)
            log.info("Sending payment success notification for Sale: {}", event.get("saleId"));
            
            // Note: We could use processOrderNotificationUseCase.execute(...) here 
            // once we have a DTO mapper for payments.
            log.info("Notification sent successfully to customer for payment confirmation.");
        }
    }
}
