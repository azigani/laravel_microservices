package com.gesco.document.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private UUID id;
    private String title;
    private DocumentType type; // INVOICE, QUOTE, CONTRACT, REPORT, OTHER
    private UUID folderId;
    private String currentVersion; // e.g., "v3"
    private UUID linkedEntityId; // e.g., saleId, customerId
    private String linkedEntityType; // "SALE", "CUSTOMER", "PRODUCT"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum DocumentType {
        INVOICE, QUOTE, CONTRACT, REPORT, DELIVERY_NOTE, OTHER
    }
}
