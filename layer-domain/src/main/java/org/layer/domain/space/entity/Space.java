package org.layer.domain.space.entity;

import static org.layer.common.exception.SpaceExceptionType.*;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.layer.domain.BaseEntity;
import org.layer.domain.space.converter.SpaceFieldConverter;
import org.layer.domain.space.exception.SpaceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space extends BaseEntity {

    private String bannerUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SpaceCategory category;

    @NotNull
    @Convert(converter = SpaceFieldConverter.class)
    private List<SpaceField> fieldList = new ArrayList<>();

    @NotNull
    private String name;

    private String introduction;

    @NotNull
    private Long leaderId;

    /**
     * Form Relationid
     */
    private Long formId;

    public void isLeaderSpace(Long memberId) {
        boolean isLeader = leaderId.equals(memberId);
        if (!isLeader) {
            throw new SpaceException(SPACE_LEADER_CANNOT_LEAVE);
        }
    }
}
