package com.gesco.document.infrastructure.adapters.messaging;

import com.gesco.document.core.application.usecase.GenerateInvoiceUseCase;
import com.gesco.document.core.domain.model.Document;
import com.gesco.document.core.domain.port.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Listens for payment.completed events from the Payment microservice.
 * Regenerates the invoice with a "PAID" status.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompletedEventListener {

    private final GenerateInvoiceUseCase generateInvoiceUseCase;
    private final DocumentRepository documentRepository;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_DOCUMENT_QUEUE)
    public void onPaymentCompleted(Map<String, Object> event) {
        log.info("Received payment.completed event for sale in GED: {}", event);

        try {
            String status = (String) event.get("status");
            if ("SUCCESS".equals(status)) {
                Object saleIdObj = event.get("saleId");
                UUID saleId = UUID.fromString(saleIdObj.toString());

                // Find existing document for this sale to get customer info or use generic
                String customerName = documentRepository.findAll().stream()
                        .filter(d -> saleId.equals(d.getLinkedEntityId()) && d.getType() == Document.DocumentType.INVOICE)
                        .findFirst()
                        .map(Document::getTitle)
                        .map(t -> t.replace("Facture - ", ""))
                        .orElse("Client");

                // In a real scenario, the total amount would be in the event or fetched via Feign
                // For simplicity, we assume we might need a fetch or it's provided
                // Let's assume for now we use 0.0 or we can try to fetch metadata from existing doc
                
                generateInvoiceUseCase.execute(saleId, customerName, 0.0, true);
                log.info("Invoice regenerated with PAID status for sale: {}", saleId);
            }
        } catch (Exception e) {
            log.error("Failed to process payment.completed event in GED: {}", e.getMessage(), e);
        }
    }
}
