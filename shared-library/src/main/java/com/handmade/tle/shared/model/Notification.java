package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "notification")
public class Notification extends BaseJpaEntity {


    @Column(name = "user_id")
    public String userId;

    @Enumerated(EnumType.STRING)
    public NotificationType type;

    public String title;

    @Column(columnDefinition = "TEXT")
    public String message;

    @Column(name = "related_entity_type")
    public String relatedEntityType;

    @Column(name = "related_entity_id")
    public String relatedEntityId;

    @Column(name = "is_read")
    public boolean isRead = false;

    @Column(name = "created_at")
    public Date createdAt = new Date();

    @ElementCollection
    @CollectionTable(name = "notification_data", joinColumns = @JoinColumn(name = "notification_id"))
    @MapKeyColumn(name = "`data_key`")
    @Column(name = "`data_value`")
    public Map<String, String> actionData = new HashMap<>();
}

enum NotificationType {
    ANSWER_TO_YOUR_PROMPT, YOUR_ANSWER_ACCEPTED, YOUR_ANSWER_UPVOTED, YOUR_ANSWER_COMMENTED,
    PROMPT_UPVOTED, PROMPT_FAVORITED, PROMPT_COMMENTED, PROMPT_CLOSED, PROMPT_REOPENED,
    BOUNTY_EXPIRING_SOON, BOUNTY_AWARDED_TO_YOU, NEW_ANSWER_TO_BOUNTIED_PROMPT,
    REPUTATION_MILESTONE, NEW_BADGE_EARNED, YOUR_CONTENT_FLAGGED, FLAG_RESOLVED,
    MENTIONED_IN_COMMENT, FOLLOWED_TAG_NEW_PROMPT
}
