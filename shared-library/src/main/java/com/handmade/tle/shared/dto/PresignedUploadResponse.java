package com.handmade.tle.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Unified response DTO for presigned upload URLs.
 * Shared across orchestration and infrastructure services.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresignedUploadResponse {
    private String uploadUrl;
    private String uploadId;
    private String fileKey;
    private Instant expiresAt;
    private String method;
    private Map<String, String> requiredHeaders;
    private String accessUrl;
    private Long maxFileSize;
    private String instructions;
}
