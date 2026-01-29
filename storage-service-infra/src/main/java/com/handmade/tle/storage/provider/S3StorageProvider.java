package com.handmade.tle.storage.provider;

import com.handmade.tle.storage.StorageProvider;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;

/**
 * AWS S3 Storage Provider Implementation
 */
@Slf4j
public class S3StorageProvider implements StorageProvider {

    private final String region;
    private final String bucketName;
    private final String accessKey;
    private final String secretKey;

    public S3StorageProvider(String region, String bucketName, String accessKey, String secretKey) {
        this.region = region;
        this.bucketName = bucketName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @PostConstruct
    public void init() {
        log.info("Initializing S3 Storage Provider - Region: {}, Bucket: {}", region, bucketName);

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Override
    public String generatePresignedUploadUrl(
            String fileKey,
            String contentType,
            Long fileSizeBytes,
            Duration expiration,
            Map<String, String> metadata) {

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .contentType(contentType)
                    .contentLength(fileSizeBytes)
                    .metadata(metadata)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toString();

        } catch (S3Exception e) {
            log.error("Failed to generate presigned upload URL for key: {}", fileKey, e);
            throw new RuntimeException("Failed to generate presigned upload URL", e);
        }
    }

    @Override
    public String generatePresignedDownloadUrl(String fileKey, Duration expiration) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toString();

        } catch (S3Exception e) {
            log.error("Failed to generate presigned download URL for key: {}", fileKey, e);
            throw new RuntimeException("Failed to generate presigned download URL", e);
        }
    }

    @Override
    public boolean fileExists(String fileKey) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public void deleteFile(String fileKey) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());
        } catch (S3Exception e) {
            log.error("Failed to delete file from S3: {}", fileKey, e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public Map<String, String> getFileMetadata(String fileKey) {
        try {
            HeadObjectResponse response = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());
            return response.metadata();
        } catch (S3Exception e) {
            log.error("Failed to get file metadata from S3: {}", fileKey, e);
            throw new RuntimeException("Failed to get file metadata", e);
        }
    }

    @Override
    public Long getFileSize(String fileKey) {
        try {
            HeadObjectResponse response = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());
            return response.contentLength();
        } catch (S3Exception e) {
            log.error("Failed to get file size from S3: {}", fileKey, e);
            throw new RuntimeException("Failed to get file size", e);
        }
    }

    @Override
    public String getPublicUrl(String fileKey) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileKey);
    }

    @PreDestroy
    public void cleanup() {
        if (s3Client != null)
            s3Client.close();
        if (s3Presigner != null)
            s3Presigner.close();
    }
}
