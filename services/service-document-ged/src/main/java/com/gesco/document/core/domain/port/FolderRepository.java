package com.gesco.document.core.domain.port;

import com.gesco.document.core.domain.model.Folder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderRepository {
    Folder save(Folder folder);

    Optional<Folder> findById(UUID id);

    List<Folder> findByParentId(UUID parentId);

    List<Folder> findRootFolders(); // parentId is null

    void deleteById(UUID id);
}
