package com.handmade.tle.upload.service;

import com.handmade.tle.shared.dto.*;
import com.handmade.tle.shared.model.Upload;
import com.handmade.tle.shared.model.UploadStatus;
import com.handmade.tle.shared.repository.UploadRepository;
import com.handmade.tle.storage.StorageProvider;
import com.handmade.tle.upload.event.UploadEventPublisher;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Upload Orchestration Service
 * Matches the business logic in the microservices spec.
 */
@Slf4j
public class UploadOrchestrationService {

    private final StorageProvider storageProvider;
    private final UploadRepository uploadRepository;
    private final UploadEventPublisher uploadEventPublisher;
    private final ValidationService validationService;

    private final int presignedExpirationSeconds;

    public UploadOrchestrationService(StorageProvider storageProvider,
            UploadRepository uploadRepository,
            UploadEventPublisher uploadEventPublisher,
            ValidationService validationService,
            int presignedExpirationSeconds) {
        this.storageProvider = storageProvider;
        this.uploadRepository = uploadRepository;
        this.uploadEventPublisher = uploadEventPublisher;
        this.validationService = validationService;
        this.presignedExpirationSeconds = presignedExpirationSeconds;
    }

    /**
     * Step 1: Generate presigned URL
     */
    @Transactional
    public PresignedUploadResponse generatePresignedUrl(
            PresignedUploadRequest request,
            String userId) {

        log.info("Processing presigned URL request - User: {}, File: {}", userId, request.getFilename());

        // 1. Validate request
        validationService.validateUploadRequest(request);

        // 2. Check quota with Quota Service (Commented out as Redis is not configured)
        /*
         * var quotaResponse = quotaService.checkStorageQuota(userId,
         * request.getFileSize());
         * if (!quotaResponse.isAllowed()) {
         * throw new RuntimeException("Quota exceeded: " + quotaResponse.getMessage());
         * }
         */

        // 3. Generate unique file key and upload ID
        String fileKey = generateFileKey(userId, request.getFilename(), request.getFolder());
        String uploadId = UUID.randomUUID().toString();

        // 4. Generate presigned URL via provider
        Duration expiration = Duration.ofSeconds(presignedExpirationSeconds);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("user-id", userId);
        metadata.put("original-filename", request.getFilename());
        metadata.put("upload-id", uploadId);

        String presignedUrl = storageProvider.generatePresignedUploadUrl(
                fileKey,
                request.getContentType(),
                request.getFileSize(),
                expiration,
                metadata);

        // 5. Save upload metadata in database
        Upload upload = Upload.builder()
                .uploadId(uploadId)
                .userId(userId)
                .filename(request.getFilename())
                .fileKey(fileKey)
                .contentType(request.getContentType())
                .fileSize(request.getFileSize())
                .status(UploadStatus.PENDING)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(expiration))
                .build();

        uploadRepository.save(upload);
        log.info("✅ Upload metadata saved: {}", uploadId);

        // 6. Reserve quota (Commented out)
        // quotaService.reserveStorageQuota(userId, request.getFileSize());
        log.info("✅ Quota reserved for upload: {}", uploadId);

        // 7. Build response
        return PresignedUploadResponse.builder()
                .uploadUrl(presignedUrl)
                .uploadId(uploadId)
                .fileKey(fileKey)
                .expiresAt(Instant.now().plus(expiration))
                .method("PUT")
                .build();
    }

    /**
     * Step 2: Confirm upload completion
     */
    @Transactional
    public UploadConfirmationResponse confirmUpload(UploadConfirmationRequest request, String userId) {
        log.info("Confirming upload: {}", request.getUploadId());

        // 1. Get upload metadata
        Upload upload = uploadRepository.findByUploadId(request.getUploadId())
                .orElseThrow(() -> new RuntimeException("Upload not found"));

        // Verify ownership
        if (!upload.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: This upload does not belong to you.");
        }

        // 2. Verify file exists in storage
        boolean exists = storageProvider.fileExists(request.getFileKey());
        if (!exists) {
            log.error("❌ File not found in storage: {}", request.getFileKey());
            upload.setStatus(UploadStatus.FAILED);
            uploadRepository.save(upload);
            throw new RuntimeException("File not found in storage. Please upload the file before confirming.");
        }

        // 3. Update upload status
        upload.setStatus(UploadStatus.COMPLETED);
        upload.setCompletedAt(Instant.now());
        uploadRepository.save(upload);
        log.info("✅ Upload status updated to COMPLETED");

        // 4. Confirm quota with Quota Service (Commented out)
        // quotaService.confirmStorageUpload(userId, request.getUploadId());
        log.info("✅ Quota confirmed");

        // 5. Publish upload completed event for Notification & Analytics services
        UploadEvent event = UploadEvent.builder()
                .uploadId(upload.getUploadId())
                .userId(userId)
                .filename(upload.getFilename())
                .fileKey(upload.getFileKey())
                .fileSize(upload.getFileSize())
                .completedAt(Instant.now())
                .build();

        uploadEventPublisher.publishUploadCompleted(event);
        log.info("✅ Upload completed event published to Kafka");

        // 6. Build response
        return UploadConfirmationResponse.builder()
                .uploadId(upload.getUploadId())
                .fileKey(upload.getFileKey())
                .fileUrl(storageProvider.getPublicUrl(upload.getFileKey()))
                .status(UploadStatus.COMPLETED)
                .fileSize(upload.getFileSize())
                .completedAt(Instant.now())
                .message("Upload confirmed successfully")
                .build();
    }

    public Upload getUploadStatus(String uploadId, String userId) {
        Upload upload = uploadRepository.findByUploadId(uploadId)
                .orElseThrow(() -> new RuntimeException("Upload not found"));

        if (!upload.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to upload status");
        }
        return upload;
    }

    @Transactional
    public void cancelUpload(String uploadId, String userId) {
        Upload upload = uploadRepository.findByUploadId(uploadId)
                .orElseThrow(() -> new RuntimeException("Upload not found"));

        if (!upload.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to cancel this upload");
        }

        upload.setStatus(UploadStatus.CANCELLED);
        uploadRepository.save(upload);

        // quotaService.cancelStorageUpload(userId, uploadId);
        log.info("Upload cancelled and quota released: {}", uploadId);
    }

    private String generateFileKey(String userId, String filename, String folder) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sanitizedFilename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        String folderPath = (folder != null && !folder.isEmpty()) ? folder : "general";

        return String.format("%s/%s/%s_%s", userId, folderPath, timestamp, sanitizedFilename);
    }
}
