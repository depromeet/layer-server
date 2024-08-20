package org.layer.domain.space.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.layer.domain.form.enums.FormTag;
import org.layer.domain.member.entity.Member;
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
    private Leader leader;
    private Long formId;
    private String formTag;
    private Long memberCount;

    private String bannerUrl;

    @QueryProjection
    public SpaceWithMemberCount(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, SpaceCategory category, List<SpaceField> fieldList, String name, String introduction, Member leader, Long formId, FormTag formTag, Long memberCount, String bannerUrl) {

        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.fieldList = fieldList;
        this.name = name;
        this.introduction = introduction;
        this.leader = Leader.builder().id(leader.getId()).name(leader.getName()).build();
        this.formId = formId;
        this.formTag = formTag != null ? formTag.getTag() : null;
        this.memberCount = memberCount;
        this.bannerUrl = bannerUrl;
    }
}