package org.layer.domain.space.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.common.dto.Meta;
import org.layer.common.exception.BaseCustomException;
import org.layer.domain.space.dto.SpaceMember;
import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;
import org.layer.domain.space.exception.SpaceException;

import java.util.List;
import java.util.Optional;

import static org.layer.common.exception.MemberExceptionType.NOT_FOUND_USER;
import static org.layer.common.exception.TokenExceptionType.INVALID_REFRESH_TOKEN;


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

            @Schema(description = "소속된 회원 수")
            Long memberCount,

            @Schema(description = "스페이스 배너 이미지")
            String bannerUrl
    ) {
        public static SpaceWithMemberCountInfo toResponse(SpaceWithMemberCount space) {
            return Optional.ofNullable(space)
                    .map(it -> SpaceWithMemberCountInfo.builder()
                            .id(it.getId())
                            .category(it.getCategory())
                            .fieldList(it.getFieldList())
                            .name(it.getName())
                            .introduction(it.getIntroduction())
                            .formId(it.getFormId())
                            .memberCount(it.getMemberCount())
                            .bannerUrl(it.getBannerUrl())
                            .build())
                    .orElseThrow(() -> new BaseCustomException(INVALID_REFRESH_TOKEN));
        }
    }

    @Builder
    @Schema()
    public record SpacePage(
            @Schema()
            List<SpaceWithMemberCountInfo> data,

            @Schema()
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
        public static SpaceMemberResponse toResponse(SpaceMember spaceMember) {
            return Optional.ofNullable(spaceMember)
                    .map(it -> SpaceMemberResponse
                            .builder()
                            .id(spaceMember.getId())
                            .name(spaceMember.getName())
                            .avatar(spaceMember.getAvatar())
                            .isLeader(spaceMember.getIsLeader())
                            .build())
                    .orElseThrow(() -> new SpaceException(NOT_FOUND_USER));
        }
    }
}
