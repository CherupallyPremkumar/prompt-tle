package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "answer")
public class Answer extends BaseJpaEntity {


    @Column(name = "prompt_id")
    public String promptId;

    @Column(name = "user_id")
    public String userId;

    @Column(name = "author_username")
    public String authorUsername;

    @Column(columnDefinition = "TEXT")
    public String body;

    public int score = 0;

    @Column(name = "is_accepted")
    public boolean isAccepted = false;

    @Column(name = "created_at")
    public Date createdAt = new Date();

    @Column(name = "updated_at")
    public Date updatedAt = new Date();

    @Column(name = "comment_count")
    public int commentCount = 0;

    @Column(name = "revision_number")
    public int revisionNumber = 1;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    public List<Attachment> attachments = new ArrayList<>();
}
