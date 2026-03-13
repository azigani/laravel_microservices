package com.gesco.sales.infrastructure.adapters.messaging;

import com.gesco.sales.core.application.usecase.CompleteSaleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final CompleteSaleUseCase completeSaleUseCase;

    @RabbitListener(queues = "payment.completed.queue")
    public void handlePaymentCompleted(Map<String, Object> event) {
        log.info("Received payment completed event: {}", event);
        
        String status = (String) event.get("status");
        if ("SUCCESS".equals(status)) {
            Object saleIdObj = event.get("saleId");
            UUID saleId;
            
            if (saleIdObj instanceof String) {
                saleId = UUID.fromString((String) saleIdObj);
            } else if (saleIdObj instanceof Integer) {
                 // Si c'est un Long/Integer venant de Java vers JSON
                 // On suppose ici que c'est un UUID String car service-sales utilise UUID
                 saleId = UUID.fromString(saleIdObj.toString());
            } else {
                saleId = UUID.fromString(saleIdObj.toString());
            }

            try {
                completeSaleUseCase.execute(saleId);
            } catch (Exception e) {
                log.error("Failed to complete sale for ID: {}", saleId, e);
            }
        }
    }
}
