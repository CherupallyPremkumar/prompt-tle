package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "prompt_revision")
public class PromptRevision extends BaseJpaEntity {


    @Column(name = "prompt_id")
    public String promptId;

    @Column(name = "revision_number")
    public int revisionNumber;

    @Column(name = "user_id")
    public String userId;

    public String title;

    @Column(columnDefinition = "TEXT")
    public String body;

    @ElementCollection
    public List<String> tags;

    @Column(name = "change_comment")
    public String changeComment;

    @Column(name = "created_at")
    public Date createdAt = new Date();
}
