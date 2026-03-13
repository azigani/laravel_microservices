package com.gesco.document.infrastructure.adapters.persistence;

import com.gesco.document.core.domain.model.DocumentVersion;
import com.gesco.document.core.domain.port.DocumentVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DocumentVersionRepositoryAdapter implements DocumentVersionRepository {

    private final JpaDocumentVersionRepository jpaRepo;

    @Override
    public DocumentVersion save(DocumentVersion v) {
        DocumentVersionEntity entity = toEntity(v);
        DocumentVersionEntity saved = jpaRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<DocumentVersion> findById(UUID id) {
        return jpaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<DocumentVersion> findByDocumentId(UUID documentId) {
        return jpaRepo.findByDocumentId(documentId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<DocumentVersion> findLatestByDocumentId(UUID documentId) {
        return jpaRepo.findLatestByDocumentId(documentId).map(this::toDomain);
    }

    private DocumentVersionEntity toEntity(DocumentVersion v) {
        return new DocumentVersionEntity(v.getId(), v.getDocumentId(), v.getVersionNumber(), v.getObjectStorageKey(),
                v.getOriginalFileName(), v.getFileSize(), v.getMimeType(), v.getCreatedAt(), v.getCreatedBy());
    }

    private DocumentVersion toDomain(DocumentVersionEntity e) {
        return DocumentVersion.builder().id(e.getId()).documentId(e.getDocumentId()).versionNumber(e.getVersionNumber())
                .objectStorageKey(e.getObjectStorageKey()).originalFileName(e.getOriginalFileName())
                .fileSize(e.getFileSize()).mimeType(e.getMimeType()).createdAt(e.getCreatedAt())
                .createdBy(e.getCreatedBy()).build();
    }
}
