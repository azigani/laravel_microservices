package com.gesco.payment.core.application.usecase;

import com.gesco.payment.core.application.port.output.PaymentProvider;
import com.gesco.payment.core.domain.model.Invoice;
import com.gesco.payment.core.domain.model.Payment;
import com.gesco.payment.infrastructure.adapters.persistence.InvoiceRepository;
import com.gesco.payment.infrastructure.adapters.persistence.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final List<PaymentProvider> paymentProviders;

    @Value("${payment.rabbitmq.payment-exchange}")
    private String paymentExchange;

    @Transactional
    public Invoice generateInvoice(Long saleId, String customerId, BigDecimal amount, String currency) {
        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .saleId(saleId)
                .customerId(customerId)
                .amount(amount)
                .currency(currency)
                .status(Invoice.InvoiceStatus.PENDING)
                .issueDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(30))
                .build();
        invoice = invoiceRepository.save(invoice);

        // Audit Logging
        sendAuditLog("INVOICE_GENERATED", "Invoice", invoice.getId().toString(), null, invoice);

        return invoice;
    }
    @Transactional(readOnly = true)
    public Invoice getInvoiceBySaleId(Long saleId) {
        return invoiceRepository.findBySaleId(saleId)
                .orElseThrow(() -> new RuntimeException("Invoice not found for saleId: " + saleId));
    }

    @Transactional
    public Payment processPayment(Long invoiceId, String paymentMethod, Map<String, Object> params) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getStatus() == Invoice.InvoiceStatus.PAID) {
            throw new RuntimeException("Invoice is already paid");
        }

        // Audit - Initiation
        sendAuditLog("PAYMENT_INITIATED", "Payment", null, null, Map.of(
                "invoiceId", invoiceId,
                "paymentMethod", paymentMethod
        ));

        // Sélection du provider
        PaymentProvider provider = paymentProviders.stream()
                .filter(p -> p.getProviderName().equalsIgnoreCase(paymentMethod))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported payment method: " + paymentMethod));

        Payment payment = provider.process(invoice, params);
        payment = paymentRepository.save(payment);

        if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
            invoice.setStatus(Invoice.InvoiceStatus.PAID);
            invoice.setPaidDate(LocalDateTime.now());
            invoiceRepository.save(invoice);

            // Notify payment completed
            rabbitTemplate.convertAndSend(paymentExchange, "payment.completed", Map.of(
                    "invoiceId", invoice.getId(),
                    "saleId", invoice.getSaleId(),
                    "status", "SUCCESS",
                    "transactionId", payment.getTransactionId()
            ));
            
            sendAuditLog("PAYMENT_SUCCESS", "Payment", payment.getId().toString(), null, payment);
        } else {
            sendAuditLog("PAYMENT_FAILED", "Payment", payment.getId().toString(), null, payment);
        }

        return payment;
    }

    private void sendAuditLog(String action, String resource, String resourceId, Object before, Object after) {
        try {
            rabbitTemplate.convertAndSend("audit.exchange", "audit.event.payment", Map.of(
                    "action", action,
                    "service", "service-payment",
                    "resource", resource,
                    "resourceId", resourceId != null ? resourceId : "N/A",
                    "before", before != null ? before : Map.of(),
                    "after", after != null ? after : Map.of(),
                    "userId", "SYSTEM",
                    "timestamp", LocalDateTime.now().toString()
            ));
        } catch (Exception e) {
            log.error("Failed to send audit log for action: {}", action, e);
        }
    }
}
