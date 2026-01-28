package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "tag")
public class Tag extends BaseJpaEntity {


    @Column(unique = true)
    public String name;

    public String description;

    @Column(name = "usage_count")
    public int usageCount = 0;

    public String category;

    @Column(name = "is_required")
    public boolean isRequired = false;
}
