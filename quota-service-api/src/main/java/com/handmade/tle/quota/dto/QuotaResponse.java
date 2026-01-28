package com.handmade.tle.quota.dto;

/**
 * response indicating if quota is available and remaining limits
 */
public class QuotaResponse {
    
    private boolean allowed;
    private Long remaining;
    private String message;

    public QuotaResponse() {}
    
    public QuotaResponse(boolean allowed, Long remaining, String message) {
        this.allowed = allowed;
        this.remaining = remaining;
        this.message = message;
    }

    public boolean isAllowed() { return allowed; }
    public void setAllowed(boolean allowed) { this.allowed = allowed; }

    public Long getRemaining() { return remaining; }
    public void setRemaining(Long remaining) { this.remaining = remaining; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static QuotaResponse allow(Long remaining) {
        return new QuotaResponse(true, remaining, "Quota available");
    }

    public static QuotaResponse deny(String message) {
        return new QuotaResponse(false, 0L, message);
    }
}
