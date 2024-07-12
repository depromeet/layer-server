package org.layer.domain.space.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SpaceWithMemberCount {

    private Long id;


    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    @NotNull
    private SpaceCategory category;

    @NotNull
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

    private Long userCount;

}
