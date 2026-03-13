package com.gesco.document.infrastructure.adapters.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "folders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    private UUID parentId;
    private UUID ownerId;

    @Column(nullable = false)
    private String path;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
