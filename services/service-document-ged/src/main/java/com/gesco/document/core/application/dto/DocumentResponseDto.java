package com.gesco.document.core.application.dto;

import com.gesco.document.core.domain.model.Document;
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
public class DocumentResponseDto {
    private UUID id;
    private String title;
    private Document.DocumentType type;
    private UUID folderId;
    private String currentVersion;
    private UUID linkedEntityId;
    private String linkedEntityType;
    private String downloadUrl; // Pre-signed MinIO URL
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
