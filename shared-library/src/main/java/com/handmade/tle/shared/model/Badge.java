package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.Date;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "badge")
public class Badge extends BaseJpaEntity {

    public String name;

    public String description;

    @Enumerated(EnumType.STRING)
    public BadgeType type;

    @Column(name = "icon_url")
    public String iconUrl;

    public String criteria;
}
