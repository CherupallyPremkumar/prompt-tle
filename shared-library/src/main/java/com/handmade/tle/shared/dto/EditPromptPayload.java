package com.handmade.tle.shared.dto;

import java.util.List;

public class EditPromptPayload {
    public String promptId;
    public String userId;
    public String newTitle;
    public String newBody;
    public List<String> newTags;
    public List<AttachmentPayload> attachments;
    public String changeComment;
    public long timestamp;
}
