package com.gesco.document.core.application.usecase;

import com.gesco.document.core.application.dto.CreateFolderRequestDto;
import com.gesco.document.core.application.dto.FolderResponseDto;
import com.gesco.document.core.domain.model.Folder;
import com.gesco.document.core.domain.port.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateFolderUseCase {

    private final FolderRepository folderRepository;

    public FolderResponseDto execute(CreateFolderRequestDto request) {
        // Build the path hierarchy
        String path = "/" + request.getName();
        if (request.getParentId() != null) {
            Folder parent = folderRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Dossier parent introuvable: " + request.getParentId()));
            path = parent.getPath() + "/" + request.getName();
        }

        Folder folder = Folder.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .parentId(request.getParentId())
                .ownerId(request.getOwnerId())
                .path(path)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Folder saved = folderRepository.save(folder);

        return FolderResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .parentId(saved.getParentId())
                .ownerId(saved.getOwnerId())
                .path(saved.getPath())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
