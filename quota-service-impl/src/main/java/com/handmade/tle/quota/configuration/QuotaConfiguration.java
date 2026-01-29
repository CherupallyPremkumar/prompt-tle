package com.handmade.tle.quota.configuration;

import com.handmade.tle.quota.QuotaManagementService;
import com.handmade.tle.quota.QuotaProvider;
import com.handmade.tle.quota.infrastructure.RedisQuotaProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class QuotaConfiguration {

    @Bean
    QuotaProvider quotaProvider(
            StringRedisTemplate redisTemplate,
            @Value("${quota.default-limit:104857600}") Long defaultQuotaLimit,
            @Value("${quota.reservation-ttl-minutes:30}") int reservationTtlMinutes) {
        return new RedisQuotaProvider(redisTemplate, defaultQuotaLimit, reservationTtlMinutes);
    }

    @Bean
    QuotaManagementService quotaManagementService(QuotaProvider quotaProvider) {
        return new QuotaManagementService(quotaProvider);
    }
}