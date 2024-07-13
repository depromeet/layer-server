package org.layer.domain.space.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

import java.time.LocalDateTime;


@Getter
@Setter
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
    private Long formId;
    private Long userCount;

    @QueryProjection
    public SpaceWithMemberCount(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, SpaceCategory category, SpaceField field, String name, String introduction, Long leaderId, Long formId, Long userCount) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.field = field;
        this.name = name;
        this.introduction = introduction;
        this.leaderId = leaderId;
        this.formId = formId;
        this.userCount = userCount;
    }
}