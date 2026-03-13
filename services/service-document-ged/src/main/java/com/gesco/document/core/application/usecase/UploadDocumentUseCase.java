package com.gesco.document.core.application.usecase;

import com.gesco.document.core.application.dto.DocumentResponseDto;
import com.gesco.document.core.application.dto.UploadDocumentRequestDto;
import com.gesco.document.core.domain.model.Document;
import com.gesco.document.core.domain.model.DocumentVersion;
import com.gesco.document.core.domain.port.DocumentRepository;
import com.gesco.document.core.domain.port.DocumentVersionRepository;
import com.gesco.document.core.domain.port.FileStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadDocumentUseCase {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;
    private final FileStoragePort fileStoragePort;

    public DocumentResponseDto execute(UploadDocumentRequestDto request, MultipartFile file) {
        try {
            // 1. Create the Document aggregate
            UUID docId = UUID.randomUUID();
            String versionLabel = "v1";
            String objectKey = buildStorageKey(request.getType(), docId, file.getOriginalFilename(), 1);

            Document document = Document.builder()
                    .id(docId)
                    .title(request.getTitle())
                    .type(request.getType())
                    .folderId(request.getFolderId())
                    .currentVersion(versionLabel)
                    .linkedEntityId(request.getLinkedEntityId())
                    .linkedEntityType(request.getLinkedEntityType())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // 2. Save metadata to PostgreSQL
            Document saved = documentRepository.save(document);

            // 3. Upload file to MinIO
            fileStoragePort.upload(objectKey, file.getInputStream(), file.getContentType(), file.getSize());

            // 4. Save the version record
            DocumentVersion version = DocumentVersion.builder()
                    .id(UUID.randomUUID())
                    .documentId(saved.getId())
                    .versionNumber(1)
                    .objectStorageKey(objectKey)
                    .originalFileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .mimeType(file.getContentType())
                    .createdAt(LocalDateTime.now())
                    .build();
            versionRepository.save(version);

            // 5. Build response with a download URL
            String downloadUrl = fileStoragePort.getPresignedUrl(objectKey, 3600);

            return DocumentResponseDto.builder()
                    .id(saved.getId())
                    .title(saved.getTitle())
                    .type(saved.getType())
                    .folderId(saved.getFolderId())
                    .currentVersion(saved.getCurrentVersion())
                    .linkedEntityId(saved.getLinkedEntityId())
                    .linkedEntityType(saved.getLinkedEntityType())
                    .downloadUrl(downloadUrl)
                    .createdAt(saved.getCreatedAt())
                    .updatedAt(saved.getUpdatedAt())
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier: " + e.getMessage(), e);
        }
    }

    private String buildStorageKey(Document.DocumentType type, UUID docId, String filename, int version) {
        String folder = type.name().toLowerCase() + "s"; // e.g., "invoices", "quotes"
        return folder + "/" + docId + "/v" + version + "/" + filename;
    }
}
