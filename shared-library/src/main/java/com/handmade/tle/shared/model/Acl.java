package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import lombok.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "acls", indexes = {
        @Index(name = "idx_acl_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Acl extends BaseJpaEntity {

    @Column(nullable = false, unique = true)
    private String name; // e.g., "READ_PROMPT", "WRITE_COMMENT"

    private String description;

    @ManyToMany(mappedBy = "acls")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
