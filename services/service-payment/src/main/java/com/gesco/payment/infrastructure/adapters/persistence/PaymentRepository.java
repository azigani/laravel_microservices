package com.gesco.payment.infrastructure.adapters.persistence;

import com.gesco.payment.core.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoiceId(Long invoiceId);

    Optional<Payment> findByTransactionId(String transactionId);
}
