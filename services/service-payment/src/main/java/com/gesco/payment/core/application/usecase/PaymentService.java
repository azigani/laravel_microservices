package com.gesco.payment.core.application.usecase;

import com.gesco.payment.core.domain.model.Invoice;
import com.gesco.payment.core.domain.model.Payment;
import com.gesco.payment.infrastructure.adapters.persistence.InvoiceRepository;
import com.gesco.payment.infrastructure.adapters.persistence.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${payment.stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${payment.rabbitmq.payment-exchange}")
    private String paymentExchange;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

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
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Payment processPayment(Long invoiceId, String sourceToken) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getStatus() == Invoice.InvoiceStatus.PAID) {
            throw new RuntimeException("Invoice is already paid");
        }

        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(invoice.getAmount())
                .currency(invoice.getCurrency())
                .paymentMethod("STRIPE")
                .status(Payment.PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .build();
        payment = paymentRepository.save(payment);

        try {
            Map<String, Object> chargeParams = new HashMap<>();
            // Stripe deals in cents
            chargeParams.put("amount", invoice.getAmount().multiply(new BigDecimal(100)).intValue());
            chargeParams.put("currency", invoice.getCurrency());
            chargeParams.put("source", sourceToken); // e.g., 'tok_visa' for testing
            chargeParams.put("description", "Payment for Invoice: " + invoice.getInvoiceNumber());

            Charge charge = Charge.create(chargeParams);

            payment.setTransactionId(charge.getId());
            if ("succeeded".equals(charge.getStatus())) {
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                invoice.setStatus(Invoice.InvoiceStatus.PAID);
                invoice.setPaidDate(LocalDateTime.now());
                invoiceRepository.save(invoice);
            } else {
                payment.setStatus(Payment.PaymentStatus.FAILED);
            }
        } catch (StripeException e) {
            log.error("Stripe payment failed", e);
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setTransactionId(e.getStripeError() != null ? e.getStripeError().getCode() : "ERROR");
        }

        return paymentRepository.save(payment);
    }
}
