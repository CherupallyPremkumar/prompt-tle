package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.Date;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "bounty")
public class Bounty extends BaseJpaEntity {


    @Column(name = "prompt_id")
    public String promptId;

    @Column(name = "sponsor_user_id")
    public String sponsorUserId;

    public int amount;

    @Column(columnDefinition = "TEXT")
    public String description;

    @Column(name = "started_at")
    public Date startedAt = new Date();

    @Column(name = "expires_at")
    public Date expiresAt;

    @Column(name = "awarded_to_answer_id")
    public String awardedToAnswerId;

    @Column(name = "awarded_to_user_id")
    public String awardedToUserId;

    @Column(name = "awarded_at")
    public Date awardedAt;

    @Enumerated(EnumType.STRING)
    public BountyStatus status = BountyStatus.ACTIVE;
}

enum BountyStatus {
    ACTIVE, AWARDED, EXPIRED, CANCELLED
}
