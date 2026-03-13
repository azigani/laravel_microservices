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
public class Folder {
    private UUID id;
    private String name;
    private UUID parentId; // null = root folder
    private UUID ownerId; // ID from identity-service
    private String path; // Computed path: /clients/gesco/invoices/2026
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
