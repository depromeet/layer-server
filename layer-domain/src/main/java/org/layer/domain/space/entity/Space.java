package org.layer.domain.space.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private SpaceCategory category;

    @NotNull
    private Field field;

    @NotNull
    private String name;

    private String introduction;

    @NotNull
    private Long leaderId;

    private Long defaultFormId;

    /**
     * Form Relationid
     */
    private Long formId;
}
