package org.layer.domain.space.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class SpaceWithMemberCount {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotNull
    private SpaceCategory category;
    @NotNull
    private List<SpaceField> fieldList;
    @NotNull
    private String name;
    private String introduction;
    @NotNull
    private Long leaderId;
    private Long formId;
    private Long memberCount;

    private String bannerUrl;

    @QueryProjection
    public SpaceWithMemberCount(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, SpaceCategory category, List<SpaceField> fieldList, String name, String introduction, Long leaderId, Long formId, Long memberCount, String bannerUrl) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.fieldList = fieldList;
        this.name = name;
        this.introduction = introduction;
        this.leaderId = leaderId;
        this.formId = formId;
        this.memberCount = memberCount;
        this.bannerUrl = bannerUrl;
    }
}