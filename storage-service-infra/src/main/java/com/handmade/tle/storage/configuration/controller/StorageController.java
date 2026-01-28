package com.handmade.tle.storage.configuration.controller;


import com.handmade.tle.shared.dto.PresignedUploadRequest;
import com.handmade.tle.shared.dto.PresignedUploadResponse;
import com.handmade.tle.storage.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Storage Service Controller
 * Focuses on direct cloud storage operations.
 */
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {

    private static final Logger log = LoggerFactory.getLogger(StorageController.class);
    private final StorageProvider storageProvider;

    @Value("${cloud.storage.expiration:300}")
    private int presignedExpirationSeconds;

    @PostMapping("/presigned-upload-url")
    public ResponseEntity<PresignedUploadResponse> generatePresignedUploadUrl(
            @RequestBody PresignedUploadRequest request,
            @RequestParam String fileKey) {
        
        log.info("Generating presigned upload URL for key: {}", fileKey);
        
        Duration expiration = Duration.ofSeconds(presignedExpirationSeconds);
        Map<String, String> metadata = new HashMap<>();
        
        String url = storageProvider.generatePresignedUploadUrl(
                fileKey, 
                request.getContentType(), 
                request.getFileSize(), 
                expiration, 
                metadata);
        
        return ResponseEntity.ok(PresignedUploadResponse.builder()
                .uploadUrl(url)
                .fileKey(fileKey)
                .expiresAt(Instant.now().plus(expiration))
                .method("PUT")
                .build());
    }

    @PostMapping("/presigned-download-url")
    public ResponseEntity<String> generatePresignedDownloadUrl(@RequestParam String fileKey) {
        log.info("Generating presigned download URL for key: {}", fileKey);
        String url = storageProvider.generatePresignedDownloadUrl(fileKey, Duration.ofHours(1));
        return ResponseEntity.ok(url);
    }

    @GetMapping("/file/exists")
    public ResponseEntity<Boolean> fileExists(@RequestParam String fileKey) {
        return ResponseEntity.ok(storageProvider.fileExists(fileKey));
    }

    @GetMapping("/file/size")
    public ResponseEntity<Long> getFileSize(@RequestParam String fileKey) {
        return ResponseEntity.ok(storageProvider.getFileSize(fileKey));
    }

    @GetMapping("/file/public-url")
    public ResponseEntity<String> getPublicUrl(@RequestParam String fileKey) {
        return ResponseEntity.ok(storageProvider.getPublicUrl(fileKey));
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam String fileKey) {
        storageProvider.deleteFile(fileKey);
        return ResponseEntity.noContent().build();
    }
}
