package org.layer.domain.space.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.layer.domain.BaseEntity;

@Getter
@Entity
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private SpaceCategory category;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SpaceField field;

    @NotNull
    private String name;

    private String introduction;

    @NotNull
    private Long leaderId;

    /**
     * Form Relationid
     */
    private Long formId;
}
