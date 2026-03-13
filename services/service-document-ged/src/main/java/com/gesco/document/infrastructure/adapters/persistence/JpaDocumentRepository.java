package com.gesco.document.infrastructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface JpaDocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    List<DocumentEntity> findByFolderId(UUID folderId);

    List<DocumentEntity> findByLinkedEntityId(UUID linkedEntityId);
}
