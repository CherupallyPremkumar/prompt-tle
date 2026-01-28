package com.handmade.tle.quota.configuration.controller;

import com.handmade.tle.quota.QuotaManagementService;
import com.handmade.tle.quota.dto.QuotaCheckRequest;
import com.handmade.tle.quota.dto.QuotaResponse;
import com.handmade.tle.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Quota Service Controller
 * Exposes endpoints for storage quota management as per the microservices spec.
 */
@RestController
@RequestMapping("/api/quota")
@RequiredArgsConstructor
public class QuotaController {

    private static final Logger log = LoggerFactory.getLogger(QuotaController.class);
    private final QuotaManagementService quotaService;

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<QuotaResponse>> checkQuota(@RequestBody QuotaCheckRequest request) {
        log.info("Checking quota for user: {}", request.getUserId());
        QuotaResponse response = quotaService.checkStorageQuota(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<String>> reserveQuota(@RequestBody QuotaCheckRequest request) {
        log.info("Reserving quota for user: {}", request.getUserId());
        String operationId = quotaService.reserveStorageQuota(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(ApiResponse.success(operationId));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmQuota(
            @RequestParam String userId,
            @RequestParam String uploadId) {
        log.info("Confirming quota for user: {}, upload: {}", userId, uploadId);
        quotaService.confirmStorageUpload(userId, uploadId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/release")
    public ResponseEntity<ApiResponse<Void>> releaseQuota(
            @RequestParam String userId,
            @RequestParam String uploadId) {
        log.info("Releasing quota for user: {}, upload: {}", userId, uploadId);
        quotaService.cancelStorageUpload(userId, uploadId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/usage/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUsage(@PathVariable String userId) {
        log.info("Fetching usage for user: {}", userId);
        // This is a placeholder for real usage stats from Redis/DB
        Map<String, Object> usage = Map.of(
            "userId", userId,
            "totalQuota", 10 * 1024 * 1024 * 1024L, // 10GB
            "usedQuota", 2 * 1024 * 1024 * 1024L,   // 2GB
            "remainingQuota", 8 * 1024 * 1024 * 1024L
        );
        return ResponseEntity.ok(ApiResponse.success(usage));
    }
}
