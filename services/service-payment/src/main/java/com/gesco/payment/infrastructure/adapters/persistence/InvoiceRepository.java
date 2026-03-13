package com.gesco.payment.infrastructure.adapters.persistence;

import com.gesco.payment.core.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findBySaleId(Long saleId);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
