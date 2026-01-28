package com.handmade.tle.shared.dto;

public class AddRevisionPayload {
    public String entityType;
    public String entityId;
    public String userId;
    public String newContent;
    public String changeComment;
    public long timestamp;
}
