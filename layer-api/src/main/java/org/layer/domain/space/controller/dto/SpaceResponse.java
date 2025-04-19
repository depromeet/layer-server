package org.layer.domain.space.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.layer.common.dto.Meta;
import org.layer.domain.form.entity.Form;
import org.layer.domain.member.entity.Member;
import org.layer.domain.space.dto.Leader;
import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class SpaceResponse {

    @Builder
    @Schema(description = "스페이스 정보 응답")
    public record SpaceWithMemberCountInfo(
            @Schema(description = "스페이스 ID")
            @NotNull
            Long id,
            @Schema(description = "프로젝트 유형 카테고리")
            @NotNull
            SpaceCategory category,
            @Schema(description = "진행중인 프로젝트 유형")
            @NotNull
            List<SpaceField> fieldList,
            @Schema(description = "이름")
            @NotNull
            String name,
            @Schema(description = "공간 설명")
            String introduction,
            @Schema(description = "설정된 회고 폼 아이디")
            Long formId,
            @Schema(description = "설정된 회고 폼 태그")
            String formTag,
            @Schema(description = "소속된 회원 수")
            Long memberCount,
            @Schema(description = "스페이스 배너 이미지")
            String bannerUrl,
            @Schema(description = "스페이스 생성 일자")
            LocalDateTime createdAt,
            @Schema(description = "스페이스 리더 아이디")
            Leader leader
    ) {
        public static SpaceWithMemberCountInfo toResponse(SpaceWithMemberCount space) {

            return SpaceWithMemberCountInfo.builder()
                .id(space.getId())
                .category(space.getCategory())
                .fieldList(space.getFieldList())
                .name(space.getName())
                .introduction(space.getIntroduction())
                .formId(space.getFormId())
                .formTag(space.getFormTag())
                .memberCount(space.getMemberCount())
                .bannerUrl(space.getBannerUrl())
                .createdAt(space.getCreatedAt())
                .leader(space.getLeader())
                .build();
        }

        public static SpaceWithMemberCountInfo of(Space space, Form form, Long memberCount, Leader leader) {
            String formTag = form != null ? form.getFormTag().getTag() : null;

            return SpaceWithMemberCountInfo.builder()
                .id(space.getId())
                .category(space.getCategory())
                .fieldList(space.getFieldList())
                .name(space.getName())
                .introduction(space.getIntroduction())
                .formId(space.getFormId())
                .formTag(formTag)
                .memberCount(memberCount)
                .bannerUrl(space.getBannerUrl())
                .createdAt(space.getCreatedAt())
                .leader(leader)
                .build();
        }
    }

    @Builder
    @Schema
    public record SpacePage(
            @Schema
            List<SpaceWithMemberCountInfo> data,

            @Schema
            Meta meta
    ) {

        public static SpacePage toResponse(List<SpaceWithMemberCountInfo> spaceInfo, Meta meta) {
            return SpacePage.builder().data(spaceInfo).meta(meta).build();
        }
    }

    @Builder
    @Schema
    public record SpaceCreateResponse(
            @Schema(title = "생성된 스페이스 아이디", description = """
                                        
                    생성 완료된 스페이스의 아이디
                                        
                    """)
            Long spaceId
    ) {
    }

    @Builder
    @Schema
    public record SpaceMemberResponse(
            @Schema(title = "맴버 아이디")
            Long id,
            @Schema(title = "멤버 프로필 사진")
            String avatar,

            @Schema(title = "멤버 이름")
            String name,

            @Schema(title = "스페이스 리더 여부")
            Boolean isLeader
    ) {
        public static SpaceMemberResponse of(Member member, boolean isLeader) {
            return SpaceMemberResponse
                .builder()
                .id(member.getId())
                .name(member.getName())
                .avatar(member.getProfileImageUrl())
                .isLeader(isLeader)
                .build();
        }
    }
}
