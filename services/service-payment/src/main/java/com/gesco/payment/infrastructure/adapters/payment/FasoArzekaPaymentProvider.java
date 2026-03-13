package com.gesco.payment.infrastructure.adapters.payment;

import com.gesco.payment.core.application.port.output.PaymentProvider;
import com.gesco.payment.core.domain.model.Invoice;
import com.gesco.payment.core.domain.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class FasoArzekaPaymentProvider implements PaymentProvider {

    @Override
    public String getProviderName() {
        return "FASO_ARZEKA";
    }

    @Override
    public Payment process(Invoice invoice, Map<String, Object> params) {
        String phoneNumber = (String) params.get("phoneNumber");
        
        log.info("Processing Faso Arzeka payment for invoice {} via phone {}", 
                invoice.getInvoiceNumber(), phoneNumber);

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number is required for Faso Arzeka payment");
        }

        // Simuler un appel API vers Faso Arzeka
        // Dans une vraie implémentation, on utiliserait un RestTemplate ou WebClient
        
        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(invoice.getAmount())
                .currency(invoice.getCurrency())
                .paymentMethod(getProviderName())
                .paymentDate(LocalDateTime.now())
                .build();

        // Simulation de réussite (si le numéro commence par 00, on simule une erreur pour les tests)
        if (phoneNumber.startsWith("00")) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setTransactionId("ERR_FA_" + UUID.randomUUID().toString().substring(0, 8));
            log.error("Faso Arzeka payment failed for phone {}", phoneNumber);
        } else {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setTransactionId("FA_" + UUID.randomUUID().toString().substring(0, 8));
            log.info("Faso Arzeka payment success for phone {}", phoneNumber);
        }

        return payment;
    }
}
