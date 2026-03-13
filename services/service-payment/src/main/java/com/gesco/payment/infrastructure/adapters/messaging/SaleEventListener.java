package com.gesco.payment.infrastructure.adapters.messaging;

import com.gesco.payment.core.application.usecase.PaymentService;
import com.gesco.payment.core.domain.model.SaleCreatedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaleEventListener {
    private final PaymentService paymentService;

    @RabbitListener(queues = "${payment.rabbitmq.invoice-queue}")
    public void handleSaleCreated(SaleCreatedDto saleEvent) {
        log.info("Received sale created event for saleId: {}", saleEvent.getId());
        try {
            // Assume default currency is EUR for now
            paymentService.generateInvoice(
                    saleEvent.getId(),
                    saleEvent.getCustomerId(),
                    saleEvent.getTotalAmount(),
                    "EUR");
            log.info("Invoice generated successfully for saleId: {}", saleEvent.getId());
        } catch (Exception e) {
            log.error("Failed to generate invoice for saleId: {}", saleEvent.getId(), e);
        }
    }
}
