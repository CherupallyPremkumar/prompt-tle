package com.handmade.tle.storage.provider;

import com.handmade.tle.storage.StorageProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Google Cloud Storage Provider Implementation
 */
@Slf4j
public class GCSStorageProvider implements StorageProvider {

    private final String projectId;
    private final String bucketName;
    private final String credentialsPath;

    public GCSStorageProvider(String projectId, String bucketName, String credentialsPath) {
        this.projectId = projectId;
        this.bucketName = bucketName;
        this.credentialsPath = credentialsPath;
    }

    private Storage storage;
    private Bucket bucket;

    @PostConstruct
    public void init() {
        log.info("Initializing GCS Storage Provider - Project: {}, Bucket: {}", projectId, bucketName);

        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(credentialsPath))
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");

            this.storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(credentials)
                    .build()
                    .getService();

            this.bucket = storage.get(bucketName);
        } catch (IOException e) {
            log.error("Failed to initialize GCS: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize GCS", e);
        }
    }

    @Override
    public String generatePresignedUploadUrl(
            String fileKey,
            String contentType,
            Long fileSizeBytes,
            Duration expiration,
            Map<String, String> metadata) {

        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, fileKey))
                .setContentType(contentType)
                .setMetadata(metadata)
                .build();

        URL signedUrl = storage.signUrl(
                blobInfo,
                expiration.toMillis(),
                TimeUnit.MILLISECONDS,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withContentType(),
                Storage.SignUrlOption.withExtHeaders(Map.of(
                        "Content-Type", contentType,
                        "Content-Length", String.valueOf(fileSizeBytes))));

        return signedUrl.toString();
    }

    @Override
    public String generatePresignedDownloadUrl(String fileKey, Duration expiration) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, fileKey)).build();

        URL signedUrl = storage.signUrl(
                blobInfo,
                expiration.toMillis(),
                TimeUnit.MILLISECONDS,
                Storage.SignUrlOption.httpMethod(HttpMethod.GET));

        return signedUrl.toString();
    }

    @Override
    public boolean fileExists(String fileKey) {
        Blob blob = storage.get(BlobId.of(bucketName, fileKey));
        return blob != null && blob.exists();
    }

    @Override
    public void deleteFile(String fileKey) {
        storage.delete(BlobId.of(bucketName, fileKey));
    }

    @Override
    public Map<String, String> getFileMetadata(String fileKey) {
        Blob blob = storage.get(BlobId.of(bucketName, fileKey));
        return blob != null ? new HashMap<>(blob.getMetadata()) : new HashMap<>();
    }

    @Override
    public Long getFileSize(String fileKey) {
        Blob blob = storage.get(BlobId.of(bucketName, fileKey));
        return blob != null ? blob.getSize() : 0L;
    }

    @Override
    public String getPublicUrl(String fileKey) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileKey);
    }
}
