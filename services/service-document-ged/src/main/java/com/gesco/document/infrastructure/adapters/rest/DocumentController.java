package com.gesco.document.infrastructure.adapters.rest;

import com.gesco.document.core.application.dto.DocumentResponseDto;
import com.gesco.document.core.application.dto.UploadDocumentRequestDto;
import com.gesco.document.core.application.usecase.GenerateInvoiceUseCase;
import com.gesco.document.core.application.usecase.UploadDocumentUseCase;
import com.gesco.document.core.domain.model.Document;
import com.gesco.document.core.domain.model.DocumentVersion;
import com.gesco.document.core.domain.port.DocumentRepository;
import com.gesco.document.core.domain.port.DocumentVersionRepository;
import com.gesco.document.core.domain.port.FileStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final UploadDocumentUseCase uploadDocumentUseCase;
    private final GenerateInvoiceUseCase generateInvoiceUseCase;
    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;
    private final FileStoragePort fileStoragePort;

    /**
     * Upload a document with a file attachment.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponseDto> uploadDocument(
            @RequestPart("metadata") UploadDocumentRequestDto metadata,
            @RequestPart("file") MultipartFile file) {
        DocumentResponseDto response = uploadDocumentUseCase.execute(metadata, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Manually trigger invoice generation (for testing or manual trigger).
     */
    @PostMapping("/generate-invoice")
    public ResponseEntity<DocumentResponseDto> generateInvoice(
            @RequestParam UUID saleId,
            @RequestParam String customerName,
            @RequestParam double totalAmount) {
        DocumentResponseDto response = generateInvoiceUseCase.execute(saleId, customerName, totalAmount);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a document by ID with download URL.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDto> getDocument(@PathVariable UUID id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable: " + id));

        String downloadUrl = versionRepository.findLatestByDocumentId(id)
                .map(v -> fileStoragePort.getPresignedUrl(v.getObjectStorageKey(), 3600))
                .orElse(null);

        return ResponseEntity.ok(DocumentResponseDto.builder()
                .id(doc.getId()).title(doc.getTitle()).type(doc.getType())
                .folderId(doc.getFolderId()).currentVersion(doc.getCurrentVersion())
                .linkedEntityId(doc.getLinkedEntityId()).linkedEntityType(doc.getLinkedEntityType())
                .downloadUrl(downloadUrl)
                .createdAt(doc.getCreatedAt()).updatedAt(doc.getUpdatedAt())
                .build());
    }

    /**
     * Get all documents in a folder.
     */
    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<DocumentResponseDto>> getDocumentsByFolder(@PathVariable UUID folderId) {
        List<DocumentResponseDto> docs = documentRepository.findByFolderId(folderId).stream()
                .map(d -> DocumentResponseDto.builder()
                        .id(d.getId()).title(d.getTitle()).type(d.getType())
                        .currentVersion(d.getCurrentVersion()).createdAt(d.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(docs);
    }

    /**
     * Get all documents linked to a business entity (sale, customer, etc.).
     */
    @GetMapping("/linked/{entityId}")
    public ResponseEntity<List<DocumentResponseDto>> getDocumentsByEntity(@PathVariable UUID entityId) {
        List<DocumentResponseDto> docs = documentRepository.findByLinkedEntityId(entityId).stream()
                .map(d -> DocumentResponseDto.builder()
                        .id(d.getId()).title(d.getTitle()).type(d.getType())
                        .linkedEntityType(d.getLinkedEntityType())
                        .currentVersion(d.getCurrentVersion()).createdAt(d.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(docs);
    }

    /**
     * Get all versions of a document.
     */
    @GetMapping("/{id}/versions")
    public ResponseEntity<List<DocumentVersion>> getVersions(@PathVariable UUID id) {
        List<DocumentVersion> versions = versionRepository.findByDocumentId(id);
        return ResponseEntity.ok(versions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
