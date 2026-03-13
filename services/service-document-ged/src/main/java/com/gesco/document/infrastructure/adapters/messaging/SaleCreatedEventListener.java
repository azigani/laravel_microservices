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

    @RabbitListener(queues = RabbitMQConfig.DOCUMENT_QUEUE)
    public void onSaleCreated(SaleCreatedEventDto event) {
        log.info("Received sale.created event for sale: {}", event.getSaleId());

        try {
            UUID saleId = event.getSaleId();
            // In a real scenario, we might call service-identity via Feign to get the customer name
            // For now we use the customerId as a placeholder or name
            String customerName = "Client ID: " + event.getCustomerId();
            double totalAmount = event.getTotalAmount() != null ? event.getTotalAmount().doubleValue() : 0.0;

            generateInvoiceUseCase.execute(saleId, customerName, totalAmount, false);
            log.info("Invoice successfully triggered for sale: {}", saleId);

        } catch (Exception e) {
            log.error("Failed to process sale.created event: {}", e.getMessage(), e);
        }
    }
}
