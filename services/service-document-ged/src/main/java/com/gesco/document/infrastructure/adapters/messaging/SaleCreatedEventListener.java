package com.gesco.document.infrastructure.adapters.messaging;

import com.gesco.document.core.application.usecase.GenerateInvoiceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Listens for sale.created events from the Sales microservice.
 * Automatically triggers invoice generation via JasperReports.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SaleCreatedEventListener {

    private final GenerateInvoiceUseCase generateInvoiceUseCase;

    @RabbitListener(queues = "document.sale.created.queue")
    public void onSaleCreated(Map<String, Object> event) {
        log.info("Received sale.created event: {}", event);

        try {
            UUID saleId = UUID.fromString((String) event.get("id"));
            String customerName = (String) event.getOrDefault("customerName", "Client inconnu");
            double totalAmount = event.get("totalAmount") != null
                    ? Double.parseDouble(event.get("totalAmount").toString())
                    : 0.0;

            generateInvoiceUseCase.execute(saleId, customerName, totalAmount);
            log.info("Invoice successfully generated for sale: {}", saleId);

        } catch (Exception e) {
            log.error("Failed to process sale.created event: {}", e.getMessage(), e);
        }
    }
}
