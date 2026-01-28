package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.Date;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "comment")
public class Comment extends BaseJpaEntity {


    @Column(name = "prompt_id")
    public String promptId;

    public String author;

    @Column(columnDefinition = "TEXT")
    public String content;

    @Column(name = "created_at")
    public Date createdAt = new Date();
}
