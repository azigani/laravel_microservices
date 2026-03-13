package com.gesco.payment.infrastructure.adapters.rest;

import com.gesco.payment.core.application.usecase.PaymentService;
import com.gesco.payment.core.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/invoice/sale/{saleId}")
    public ResponseEntity<Invoice> getInvoiceBySaleId(@PathVariable Long saleId) {
        return ResponseEntity.ok(paymentService.getInvoiceBySaleId(saleId));
    }

    @PostMapping("/invoice/{invoiceId}/pay")
    public ResponseEntity<Payment> payInvoice(
            @PathVariable Long invoiceId,
            @RequestBody Map<String, Object> payload) {

        String paymentMethod = (String) payload.getOrDefault("method", "STRIPE");
        
        // On passe tout le reste du payload comme paramètres au provider
        Map<String, Object> params = new HashMap<>(payload);
        params.remove("method"); 

        Payment payment = paymentService.processPayment(invoiceId, paymentMethod, params);
        return ResponseEntity.ok(payment);
    }
}
