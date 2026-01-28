package com.handmade.tle.shared.dto;

public class RollbackRevisionPayload {
    public String entityType;
    public String entityId;
    public int targetRevisionNumber;
    public String userId;
    public String reason;
    public long timestamp;
}
