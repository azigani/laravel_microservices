package com.gesco.document.infrastructure.adapters.persistence;

import com.gesco.document.core.domain.model.Document;
import com.gesco.document.core.domain.port.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryAdapter implements DocumentRepository {

    private final JpaDocumentRepository jpaRepo;

    @Override
    public Document save(Document doc) {
        DocumentEntity entity = toEntity(doc);
        DocumentEntity saved = jpaRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Document> findById(UUID id) {
        return jpaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Document> findByFolderId(UUID folderId) {
        return jpaRepo.findByFolderId(folderId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Document> findByLinkedEntityId(UUID linkedEntityId) {
        return jpaRepo.findByLinkedEntityId(linkedEntityId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }

    private DocumentEntity toEntity(Document d) {
        DocumentEntity e = new DocumentEntity();
        e.setId(d.getId());
        e.setTitle(d.getTitle());
        e.setType(d.getType().name());
        e.setFolderId(d.getFolderId());
        e.setCurrentVersion(d.getCurrentVersion());
        e.setLinkedEntityId(d.getLinkedEntityId());
        e.setLinkedEntityType(d.getLinkedEntityType());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }

    private Document toDomain(DocumentEntity e) {
        return Document.builder()
                .id(e.getId())
                .title(e.getTitle())
                .type(Document.DocumentType.valueOf(e.getType()))
                .folderId(e.getFolderId())
                .currentVersion(e.getCurrentVersion())
                .linkedEntityId(e.getLinkedEntityId())
                .linkedEntityType(e.getLinkedEntityType())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
