package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import lombok.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "prompt_vote", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "prompt_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptVote extends BaseJpaEntity {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "prompt_id", nullable = false)
    private String promptId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;
}
