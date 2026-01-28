package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import java.util.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "user_reputation")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class UserReputation extends BaseJpaEntity {
    
    public String getUserId(){
        return getId();
    }

    public void setUserId(String userId){
        setId(userId);
    }

    @Column(name = "total_reputation")
    public int totalReputation = 0;

    @ElementCollection
    @CollectionTable(name = "user_tag_reputation", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "tag_name")
    @Column(name = "points")
    public Map<String, Integer> reputationByTag = new HashMap<>();

    @ManyToMany
    @JoinTable(name = "user_badge", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "badge_id"))
    public List<Badge> badges = new ArrayList<>();
}
