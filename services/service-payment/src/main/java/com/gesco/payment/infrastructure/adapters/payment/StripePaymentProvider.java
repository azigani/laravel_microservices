package com.gesco.payment.infrastructure.adapters.payment;

import com.gesco.payment.core.application.port.output.PaymentProvider;
import com.gesco.payment.core.domain.model.Invoice;
import com.gesco.payment.core.domain.model.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class StripePaymentProvider implements PaymentProvider {

    @Value("${payment.stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public String getProviderName() {
        return "STRIPE";
    }

    @Override
    public Payment process(Invoice invoice, Map<String, Object> params) {
        String sourceToken = (String) params.getOrDefault("source", "tok_visa");

        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(invoice.getAmount())
                .currency(invoice.getCurrency())
                .paymentMethod(getProviderName())
                .status(Payment.PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .build();

        try {
            Map<String, Object> chargeParams = new HashMap<>();
            // Stripe deals in cents
            chargeParams.put("amount", invoice.getAmount().multiply(new BigDecimal(100)).intValue());
            chargeParams.put("currency", invoice.getCurrency());
            chargeParams.put("source", sourceToken);
            chargeParams.put("description", "Payment for Invoice: " + invoice.getInvoiceNumber());

            Charge charge = Charge.create(chargeParams);

            payment.setTransactionId(charge.getId());
            if ("succeeded".equals(charge.getStatus())) {
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
            } else {
                payment.setStatus(Payment.PaymentStatus.FAILED);
                log.warn("Stripe payment status: {}", charge.getStatus());
            }
        } catch (StripeException e) {
            log.error("Stripe payment failed", e);
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setTransactionId(e.getStripeError() != null ? e.getStripeError().getCode() : "ERROR");
        }

        return payment;
    }
}
