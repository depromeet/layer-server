package org.layer.domain.reaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.common.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetrospectReaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long reactionId;

    @NotNull
    private Long answerId;

    @NotNull
    private Long memberId;

    @Builder
    public RetrospectReaction(Long reactionId, Long answerId, Long memberId) {
        this.reactionId = reactionId;
        this.answerId = answerId;
        this.memberId = memberId;
    }
}
