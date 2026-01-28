package com.handmade.tle.quota;

import com.handmade.tle.quota.dto.QuotaCheckRequest;
import com.handmade.tle.quota.dto.QuotaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Quota Management Service
 * Orchestrates quota operations and provides a clean entry point for other modules.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuotaManagementService {

    private final QuotaProvider quotaProvider;

    public QuotaResponse checkStorageQuota(String userId, Long fileSize) {
        log.debug("Checking storage quota for user: {} ({} bytes)", userId, fileSize);
        return quotaProvider.checkQuota(new QuotaCheckRequest(userId, fileSize, "STORAGE_UPLOAD"));
    }

    public String reserveStorageQuota(String userId, Long fileSize) {
        String operationId = UUID.randomUUID().toString();
        quotaProvider.reserveQuota(userId, fileSize, operationId);
        return operationId;
    }

    public void confirmStorageUpload(String userId, String operationId) {
        quotaProvider.confirmQuota(userId, operationId);
    }

    public void cancelStorageUpload(String userId, String operationId) {
        quotaProvider.releaseQuota(userId, operationId);
    }
}
