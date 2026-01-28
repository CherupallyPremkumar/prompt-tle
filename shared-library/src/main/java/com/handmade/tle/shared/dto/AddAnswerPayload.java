package com.handmade.tle.shared.dto;

import java.util.List;

public class AddAnswerPayload {
    public String promptId;
    public String userId;
    public String authorUsername;
    public String body;
    public List<AttachmentPayload> attachments;
    public long timestamp;
}
