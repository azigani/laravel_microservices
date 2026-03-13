package com.gesco.document.infrastructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaDocumentVersionRepository extends JpaRepository<DocumentVersionEntity, UUID> {
    List<DocumentVersionEntity> findByDocumentId(UUID documentId);

    @Query("SELECT dv FROM DocumentVersionEntity dv WHERE dv.documentId = :documentId ORDER BY dv.versionNumber DESC LIMIT 1")
    Optional<DocumentVersionEntity> findLatestByDocumentId(UUID documentId);
}
