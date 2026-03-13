package com.gesco.payment.infrastructure.adapters.rest;

import com.gesco.payment.core.application.usecase.PaymentService;
import com.gesco.payment.core.domain.model.Invoice;
import com.gesco.payment.core.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/invoice/{invoiceId}/pay")
    public ResponseEntity<Payment> payInvoice(
            @PathVariable Long invoiceId,
            @RequestBody Map<String, String> payload) {

        String sourceToken = payload.get("source"); // Provide Stripe token here
        if (sourceToken == null || sourceToken.isEmpty()) {
            // fallback token for testing
            sourceToken = "tok_visa";
        }

        Payment payment = paymentService.processPayment(invoiceId, sourceToken);
        return ResponseEntity.ok(payment);
    }
}
