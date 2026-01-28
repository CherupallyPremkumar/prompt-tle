package com.handmade.tle.quota.dto;

/**
 * Request to check if a user has sufficient quota
 */
public class QuotaCheckRequest {
    
    private String userId;
    private Long amount;
    private String operationType;

    public QuotaCheckRequest() {}
    
    public QuotaCheckRequest(String userId, Long amount, String operationType) {
        this.userId = userId;
        this.amount = amount;
        this.operationType = operationType;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
}
