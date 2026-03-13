package com.gesco.document.infrastructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface JpaFolderRepository extends JpaRepository<FolderEntity, UUID> {
    List<FolderEntity> findByParentId(UUID parentId);

    List<FolderEntity> findByParentIdIsNull(); // Root folders
}
