package com.handmade.tle.quota;

import com.handmade.tle.quota.dto.QuotaCheckRequest;
import com.handmade.tle.quota.dto.QuotaResponse;

/**
 * Quota Provider Interface - Strategy Pattern
 * Defines methods for checking and updating user quotas.
 */
public interface QuotaProvider {
    
    /**
     * Check if a user has sufficient quota for an operation
     */
    QuotaResponse checkQuota(QuotaCheckRequest request);
    
    /**
     * Reserve quota for a pending operation
     */
    void reserveQuota(String userId, Long amount, String operationId);
    
    /**
     * Confirm previously reserved quota
     */
    void confirmQuota(String userId, String operationId);
    
    /**
     * Release previously reserved quota (e.g., if operation failed or was cancelled)
     */
    void releaseQuota(String userId, String operationId);
    
    /**
     * Increment usage directly (without reservation)
     */
    void incrementUsage(String userId, Long amount);
}
