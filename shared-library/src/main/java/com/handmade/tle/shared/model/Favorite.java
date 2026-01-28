package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "favorite")
public class Favorite extends BaseJpaEntity {


    @Column(name = "user_id")
    public String userId;

    @Column(name = "prompt_id")
    public String promptId;

    @Column(name = "added_at")
    public Date addedAt = new Date();

    @ElementCollection
    @Column(name = "custom_tag")
    public List<String> customTags = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    public String notes;
}
