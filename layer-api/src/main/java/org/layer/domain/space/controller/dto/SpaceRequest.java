package org.layer.domain.space.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.layer.common.annotation.AtLeastNotNull;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

import java.util.List;
import java.util.Optional;

@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceRequest {

    @Schema(description = "스페이스 생성하기")
    public record CreateSpaceRequest(

            @Schema(description = "스페이스 이미지 주소")
            String bannerUrl,
            @Schema(description = "프로젝트 유형 카테고리", example = "INDIVIDUAL")
            @NotNull
            SpaceCategory category,
            @Schema(description = "진행중인 프로젝트 유형")

            @NotNull
            List<SpaceField> fieldList,

            @Schema(description = "이름")
            @NotNull
            String name,

            @Schema(description = "공간 설명")
            String introduction
    ) {
        public Space toEntity(Long memberId) {
            return Space.builder()
                    .category(category)
                    .fieldList(fieldList)
                    .name(name)
                    .introduction(introduction)
                    .leaderId(memberId)
                    .bannerUrl(bannerUrl)
                    .build();
        }
    }

    @AtLeastNotNull(min = 2)
    @Schema(description = "스페이스 수정하기")
    public record UpdateSpaceRequest(
            @Schema(description = "수정하고자 하는 배너 주소")
            String bannerUrl,

            @Schema(description = "수정하고자 하는 스페이스 아이디")
            @NotNull
            Long id,

            @Schema(description = "프로젝트 유형 카테고리", example = "INDIVIDUAL", nullable = true)
            SpaceCategory category,
            @Schema(description = "진행중인 프로젝트 유형", nullable = true)

            List<SpaceField> fieldList,
            @Schema(description = "이름", nullable = true)

            String name,

            @Schema(description = "공간 설명", nullable = true)
            String introduction
    ) {

        public Space toEntity(Long memberId) {
            return Space.builder()
                    .id(id)
                    .category(category)
                    .fieldList(fieldList)
                    .name(name)
                    .introduction(introduction)
                    .leaderId(memberId)
                    .bannerUrl(bannerUrl)
                    .build();
        }
    }

    @Schema(description = "내가 속한 스페이스 조회")
    public record GetSpaceRequest(
            @Schema(description = "커서 아이디")
            Long cursorId,

            @Schema(description = "조회하고자 하는 스페이스 타입")
            Optional<SpaceCategory> category,

            @Schema(description = "페이지 사이즈", defaultValue = "1")
            int pageSize

    ) {
        public GetSpaceRequest {
            if (pageSize <= 0) {
                pageSize = 1;
            }
            if (cursorId == null) {
                cursorId = 0L;
            }
        }

    }
}
