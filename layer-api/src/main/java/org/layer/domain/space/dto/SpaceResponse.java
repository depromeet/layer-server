package org.layer.domain.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.common.dto.Meta;
import org.layer.common.exception.BaseCustomException;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

import java.util.List;
import java.util.Optional;

import static org.layer.domain.auth.exception.TokenExceptionType.INVALID_REFRESH_TOKEN;

public class SpaceResponse {
  

    @Builder
    @Schema(description = "스페이스 정보 응답")
    public record SpaceWithUserCountInfo(
            @Schema(description = "스페이스 ID")
            @NotNull
            Long id,
            @Schema(description = "프로젝트 유형 카테고리")
            @NotNull
            SpaceCategory category,
            @Schema(description = "진행중인 프로젝트 유형")
            @NotNull
            SpaceField field,

            @Schema(description = "이름")
            @NotNull
            String name,

            @Schema(description = "공간 설명")
            String introduction,

            @Schema(description = "설정된 회고 폼 아이디")
            Long formId,

            @Schema(description = "소속된 회원 수")
            Long userCount
    ) {
        public static SpaceWithUserCountInfo toResponse(SpaceWithMemberCount space) {
            return Optional.ofNullable(space)
                    .map(it -> SpaceWithUserCountInfo.builder().id(it.getId()).category(it.getCategory())
                            .field(it.getField()).name(it.getName()).introduction(it.getIntroduction())
                            .formId(it.getFormId()).userCount(it.getUserCount()).build())
                    .orElseThrow(() -> new BaseCustomException(INVALID_REFRESH_TOKEN));
        }
    }

    @Builder
    @Schema()
    public record SpacePage(
            @Schema()
            List<SpaceWithUserCountInfo> data,

            @Schema()
            Meta meta
    ) {

        public static SpacePage toResponse(List<SpaceWithUserCountInfo> spaceInfo, Meta meta) {
            return SpacePage.builder().data(spaceInfo).meta(meta).build();
        }
    }
}
