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
public class CreateFolderRequestDto {
    @NotBlank(message = "Le nom du dossier est obligatoire")
    private String name;

    private UUID parentId; // null = root folder

    private UUID ownerId;
}
