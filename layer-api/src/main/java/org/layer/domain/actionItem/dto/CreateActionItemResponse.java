package org.layer.domain.actionItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
@Schema(description = "액션 아이템 생성 응답")
public record CreateActionItemResponse(
        @Schema(description = "액션 아이템 생성 회원 ID")
        @NotNull
        Long memberId,
        @Schema(description = "액션 아이템과 매칭되는 회고가 진행중인 스페이스 ID")
        @NotNull
        Long spaceId) {
}
