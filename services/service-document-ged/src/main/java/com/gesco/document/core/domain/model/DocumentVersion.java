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
public class DocumentVersion {
    private UUID id;
    private UUID documentId;
    private int versionNumber; // 1, 2, 3...
    private String objectStorageKey; // Path in MinIO: e.g., "invoices/2026/INV-0045-v1.pdf"
    private String originalFileName;
    private long fileSize;
    private String mimeType;
    private LocalDateTime createdAt;
    private UUID createdBy; // User ID from identity-service
}
