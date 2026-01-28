package com.handmade.tle.quota.infrastructure;

import com.handmade.tle.quota.QuotaProvider;
import com.handmade.tle.quota.dto.QuotaCheckRequest;
import com.handmade.tle.quota.dto.QuotaResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis implementation of QuotaProvider
 * Uses Redis atomic operations for high-performance usage tracking.
 */
@Service
@RequiredArgsConstructor
public class RedisQuotaProvider implements QuotaProvider {

    private static final Logger log = LoggerFactory.getLogger(RedisQuotaProvider.class);

    private final StringRedisTemplate redisTemplate;

    @Value("${quota.default-limit:104857600}") // 100MB Default
    private Long defaultQuotaLimit;

    @Value("${quota.reservation-ttl-minutes:30}")
    private int reservationTtlMinutes;

    private static final String USAGE_KEY_PREFIX = "quota:usage:";
    private static final String RESERVATION_KEY_PREFIX = "quota:resv:";

    @Override
    public QuotaResponse checkQuota(QuotaCheckRequest request) {
        String usageKey = USAGE_KEY_PREFIX + request.getUserId();
        String usageStr = redisTemplate.opsForValue().get(usageKey);
        long currentUsage = (usageStr != null) ? Long.parseLong(usageStr) : 0L;
        
        long totalPending = getPendingReservations(request.getUserId());
        long anticipatedUsage = currentUsage + totalPending + request.getAmount();

        if (anticipatedUsage > defaultQuotaLimit) {
            log.warn("Quota denied for user: {}. Usage: {}, Pending: {}, Requested: {}, Limit: {}", 
                    request.getUserId(), currentUsage, totalPending, request.getAmount(), defaultQuotaLimit);
            return QuotaResponse.deny("Storage quota exceeded. Please upgrade your plan.");
        }

        return QuotaResponse.allow(defaultQuotaLimit - anticipatedUsage);
    }

    @Override
    public void reserveQuota(String userId, Long amount, String operationId) {
        String resvKey = RESERVATION_KEY_PREFIX + userId + ":" + operationId;
        redisTemplate.opsForValue().set(resvKey, String.valueOf(amount), reservationTtlMinutes, TimeUnit.MINUTES);
        log.debug("Reserved {} bytes for user: {} (Op: {})", amount, userId, operationId);
    }

    @Override
    public void confirmQuota(String userId, String operationId) {
        String resvKey = RESERVATION_KEY_PREFIX + userId + ":" + operationId;
        String resvStr = redisTemplate.opsForValue().get(resvKey);
        
        if (resvStr != null) {
            long amount = Long.parseLong(resvStr);
            incrementUsage(userId, amount);
            redisTemplate.delete(resvKey);
            log.info("Confirmed quota for user: {} (Op: {}) - {} bytes", userId, operationId, amount);
        }
    }

    @Override
    public void releaseQuota(String userId, String operationId) {
        String resvKey = RESERVATION_KEY_PREFIX + userId + ":" + operationId;
        redisTemplate.delete(resvKey);
        log.debug("Released reservation for user: {} (Op: {})", userId, operationId);
    }

    @Override
    public void incrementUsage(String userId, Long amount) {
        String usageKey = USAGE_KEY_PREFIX + userId;
        redisTemplate.opsForValue().increment(usageKey, amount);
    }

    private long getPendingReservations(String userId) {
        return 0; // Simplified for initial version
    }
}
