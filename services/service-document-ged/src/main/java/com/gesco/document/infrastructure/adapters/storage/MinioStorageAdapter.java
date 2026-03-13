package com.gesco.document.infrastructure.adapters.storage;

import com.gesco.document.core.domain.port.FileStoragePort;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * MinIO implementation of the FileStoragePort.
 * Handles uploading, downloading (presigned URLs), and deleting files.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioStorageAdapter implements FileStoragePort {

    private final MinioClient minioClient;
    private final String bucketName;

    @Override
    public void upload(String objectKey, InputStream inputStream, String contentType, long size) {
        try {
            // Ensure bucket exists
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket '{}' created.", bucketName);
            }

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectKey)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build());

            log.info("File uploaded to MinIO: {}/{}", bucketName, objectKey);
        } catch (Exception e) {
            throw new RuntimeException("Erreur MinIO upload: " + e.getMessage(), e);
        }
    }

    @Override
    public String getPresignedUrl(String objectKey, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectKey)
                    .expiry(expirySeconds, TimeUnit.SECONDS)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Erreur MinIO presigned URL: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String objectKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectKey)
                    .build());
            log.info("File deleted from MinIO: {}/{}", bucketName, objectKey);
        } catch (Exception e) {
            throw new RuntimeException("Erreur MinIO delete: " + e.getMessage(), e);
        }
    }
}
