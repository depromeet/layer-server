package org.layer.domain.admin.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdminSpaceCountGetResponse", description = "우리 팀이 만든 스페이스를 제외하고 기간 내에 만들어진 스페이스 개수를 리턴합니다.")
public record AdminSpaceCountGetResponse(
        @Schema(description = "기간 내에 만드러진 스페이스 개수", example = "11")
        Long spaceCount) {
}
