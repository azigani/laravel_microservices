package com.gesco.document.infrastructure.adapters.persistence;

import com.gesco.document.core.domain.model.Folder;
import com.gesco.document.core.domain.port.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FolderRepositoryAdapter implements FolderRepository {

    private final JpaFolderRepository jpaRepo;

    @Override
    public Folder save(Folder folder) {
        FolderEntity entity = toEntity(folder);
        FolderEntity saved = jpaRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Folder> findById(UUID id) {
        return jpaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Folder> findByParentId(UUID parentId) {
        return jpaRepo.findByParentId(parentId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Folder> findRootFolders() {
        return jpaRepo.findByParentIdIsNull().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }

    private FolderEntity toEntity(Folder f) {
        return new FolderEntity(f.getId(), f.getName(), f.getParentId(), f.getOwnerId(), f.getPath(), f.getCreatedAt(),
                f.getUpdatedAt());
    }

    private Folder toDomain(FolderEntity e) {
        return Folder.builder().id(e.getId()).name(e.getName()).parentId(e.getParentId()).ownerId(e.getOwnerId())
                .path(e.getPath()).createdAt(e.getCreatedAt()).updatedAt(e.getUpdatedAt()).build();
    }
}
