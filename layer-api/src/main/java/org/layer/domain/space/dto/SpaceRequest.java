package org.layer.domain.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

import java.util.Optional;

public class SpaceRequest {

    //    @Builder
    @Schema(description = "스페이스 생성하기")
    public record CreateSpaceRequest(
            @Schema(description = "프로젝트 유형 카테고리", example = "INDIVIDUAL")
            @NotNull
            SpaceCategory category,
            @Schema(description = "진행중인 프로젝트 유형")
            @NotNull
            SpaceField field,
            @Schema(description = "이름")
            @NotNull
            String name,

            @Schema(description = "공간 설명")
            String introduction
    ) {
        public Space toEntity(Long memberId) {
            return Space.builder()
                    .category(category)
                    .field(field)
                    .name(name)
                    .introduction(introduction)
                    .leaderId(memberId)
                    .build();
        }
    }

    @Schema(description = "내가 속한 스페이스 조회")
    public record GetSpaceRequest(
            @Schema(description = "커서 아이디")
            Long cursorId,

            @Schema(description = "조회하고자 하는 스페이스 타입")
            Optional<SpaceCategory> category,

            @Schema(description = "페이지 사이즈")
            int pageSize

    ) {

    }
}
