package com.gesco.document.core.domain.port;

import java.io.InputStream;

public interface FileStoragePort {
    /**
     * Uploads a file to the storage backend (MinIO).
     * 
     * @param objectKey   unique key (path) in the bucket, e.g.
     *                    "invoices/2026/INV-001.pdf"
     * @param inputStream file content
     * @param contentType MIME type
     * @param size        file size in bytes
     */
    void upload(String objectKey, InputStream inputStream, String contentType, long size);

    /**
     * Returns a pre-signed URL for downloading the object.
     * 
     * @param objectKey     unique key in the bucket
     * @param expirySeconds how long the URL is valid
     */
    String getPresignedUrl(String objectKey, int expirySeconds);

    /**
     * Deletes a file from the storage backend.
     */
    void delete(String objectKey);
}
