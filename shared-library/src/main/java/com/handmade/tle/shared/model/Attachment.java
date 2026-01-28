package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "attachment")
public class Attachment extends BaseJpaEntity {


    @Column(name = "url", length = 1000)
    public String url;

    public String caption;

    @Column(name = "mime_type")
    public String mimeType;

    @Column(name = "entity_type")
    public String entityType; // "PROMPT" or "ANSWER"

    @Column(name = "entity_id")
    public String entityId;
}
