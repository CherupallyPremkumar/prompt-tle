package com.handmade.tle.shared.dto;

import java.util.List;

public class AddFavoritePayload {
    public String promptId;
    public String userId;
    public List<String> customTags;
    public String notes;
    public long timestamp;
}
