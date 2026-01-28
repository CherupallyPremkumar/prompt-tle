package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.Date;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "flag")
public class Flag extends BaseJpaEntity {


    @Column(name = "entity_type")
    public String entityType; // "PROMPT" or "ANSWER"

    @Column(name = "entity_id")
    public String entityId;

    @Column(name = "user_id")
    public String userId;

    @Enumerated(EnumType.STRING)
    public FlagReason reason;

    @Column(columnDefinition = "TEXT")
    public String details;

    @Enumerated(EnumType.STRING)
    public FlagStatus status = FlagStatus.PENDING;

    @Column(name = "created_at")
    public Date createdAt = new Date();

    @Column(name = "resolved_at")
    public Date resolvedAt;

    @Column(name = "resolved_by_user_id")
    public String resolvedByUserId;

    public String resolution;
}

enum FlagReason {
    SPAM, OFFENSIVE, LOW_QUALITY, DUPLICATE, OFF_TOPIC, NEEDS_IMPROVEMENT, OUTDATED, INCORRECT
}

enum FlagStatus {
    PENDING, HELPFUL, DECLINED, DISPUTED
}
