package com.gesco.document.core.application.dto;

import com.gesco.document.core.domain.model.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadDocumentRequestDto {
    @NotBlank(message = "Le titre du document est obligatoire")
    private String title;

    @NotNull(message = "Le type de document est obligatoire")
    private Document.DocumentType type;

    private UUID folderId;

    // Link to a business entity from another microservice
    private UUID linkedEntityId;
    private String linkedEntityType; // "SALE", "CUSTOMER", "PRODUCT"
}
