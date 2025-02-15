package org.layer.domain.admin.retrospect.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminSpaceCountGetResponse", description = "우리 팀이 만든 스페이스에서 진행된 회고를 제외하고 기간 내에 시작된 회고 수를 리턴합니다.")
public record AdminRetrospectCountGetResponse(
        @Schema(description = "기간 내에 만들어진 회고 개수", example = "20")
        Long retrospectCount) {
}