package com.gesco.document.core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderResponseDto {
    private UUID id;
    private String name;
    private UUID parentId;
    private UUID ownerId;
    private String path;
    private LocalDateTime createdAt;
    private List<FolderResponseDto> subFolders;
    private List<DocumentResponseDto> documents;
}
