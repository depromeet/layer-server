package org.layer.domain.space.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.BaseEntity;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSpaceRelation extends BaseEntity {
    @NotNull
    private Long memberId;

    @NotNull
    private Long spaceId;
}
