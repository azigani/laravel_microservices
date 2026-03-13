package com.gesco.document.infrastructure.adapters.rest;

import com.gesco.document.core.application.dto.CreateFolderRequestDto;
import com.gesco.document.core.application.dto.FolderResponseDto;
import com.gesco.document.core.application.usecase.CreateFolderUseCase;
import com.gesco.document.core.domain.model.Folder;
import com.gesco.document.core.domain.port.FolderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final CreateFolderUseCase createFolderUseCase;
    private final FolderRepository folderRepository;

    @PostMapping
    public ResponseEntity<FolderResponseDto> createFolder(@Valid @RequestBody CreateFolderRequestDto request) {
        FolderResponseDto response = createFolderUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FolderResponseDto>> getRootFolders() {
        List<FolderResponseDto> folders = folderRepository.findRootFolders().stream()
                .map(f -> FolderResponseDto.builder()
                        .id(f.getId()).name(f.getName()).parentId(f.getParentId())
                        .ownerId(f.getOwnerId()).path(f.getPath()).createdAt(f.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderResponseDto> getFolder(@PathVariable UUID id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable: " + id));
        List<FolderResponseDto> subFolders = folderRepository.findByParentId(id).stream()
                .map(f -> FolderResponseDto.builder().id(f.getId()).name(f.getName()).path(f.getPath()).build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(FolderResponseDto.builder()
                .id(folder.getId()).name(folder.getName()).parentId(folder.getParentId())
                .ownerId(folder.getOwnerId()).path(folder.getPath()).createdAt(folder.getCreatedAt())
                .subFolders(subFolders)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable UUID id) {
        folderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
