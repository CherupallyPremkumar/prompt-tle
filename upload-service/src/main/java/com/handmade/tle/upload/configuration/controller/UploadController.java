package com.handmade.tle.upload.configuration.controller;

import com.handmade.tle.shared.dto.*;
import com.handmade.tle.shared.model.Upload;
import com.handmade.tle.upload.service.UploadOrchestrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Upload Service Controller
 * Matches the endpoints and signatures in the microservices spec.
 */
@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);
    private final UploadOrchestrationService orchestrationService;

    /**
     * Generate presigned URL for upload
     */
    @PostMapping("/presigned-url")
    public ResponseEntity<ApiResponse<PresignedUploadResponse>> generatePresignedUrl(
            @Valid @RequestBody PresignedUploadRequest request,
            Authentication authentication) {
        
        String userId = authentication.getName();
        log.info("Generating presigned URL for user: {}, file: {}", userId, request.getFilename());
        
        PresignedUploadResponse response = orchestrationService.generatePresignedUrl(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Confirm upload completion
     */
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<UploadConfirmationResponse>> confirmUpload(
            @Valid @RequestBody UploadConfirmationRequest request,
            Authentication authentication) {
        
        String userId = authentication.getName();
        log.info("Confirming upload: {} for user: {}", request.getUploadId(), userId);
        
        UploadConfirmationResponse response = orchestrationService.confirmUpload(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get upload status
     */
    @GetMapping("/{uploadId}")
    public ResponseEntity<ApiResponse<Upload>> getUploadStatus(
            @PathVariable String uploadId,
            Authentication authentication) {
        
        String userId = authentication.getName();
        log.info("Fetching status for upload: {} (User: {})", uploadId, userId);
        
        Upload upload = orchestrationService.getUploadStatus(uploadId, userId);
        return ResponseEntity.ok(ApiResponse.success(upload));
    }

    /**
     * Cancel upload
     */
    @DeleteMapping("/{uploadId}")
    public ResponseEntity<ApiResponse<Void>> cancelUpload(
            @PathVariable String uploadId,
            Authentication authentication) {
        
        String userId = authentication.getName();
        log.info("Cancelling upload: {} for user: {}", uploadId, userId);
        
        orchestrationService.cancelUpload(uploadId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
