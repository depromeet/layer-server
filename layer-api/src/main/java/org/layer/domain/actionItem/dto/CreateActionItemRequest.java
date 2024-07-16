package org.layer.domain.actionItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "액션 아이템 생성 요정")
public record CreateActionItemRequest(@NotNull
                                      @Schema(description = "액션 아이템과 매핑되는 회고 ID")
                                      Long retrospectId,
                                      @NotNull
                                      @Schema(description = "액션 아이템 내용")
                                      @NotNull String content) {
}
