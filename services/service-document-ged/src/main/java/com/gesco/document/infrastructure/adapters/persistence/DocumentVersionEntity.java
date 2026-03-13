package com.gesco.document.infrastructure.adapters.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersionEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID documentId;

    private int versionNumber;
    private String objectStorageKey;
    private String originalFileName;
    private long fileSize;
    private String mimeType;
    private LocalDateTime createdAt;
    private UUID createdBy;
}
