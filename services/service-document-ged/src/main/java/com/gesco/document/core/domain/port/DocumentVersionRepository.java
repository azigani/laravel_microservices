package com.gesco.document.core.domain.port;

import com.gesco.document.core.domain.model.DocumentVersion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentVersionRepository {
    DocumentVersion save(DocumentVersion version);

    Optional<DocumentVersion> findById(UUID id);

    List<DocumentVersion> findByDocumentId(UUID documentId);

    Optional<DocumentVersion> findLatestByDocumentId(UUID documentId);
}
