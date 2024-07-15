package org.layer.domain.actionItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


@Schema(description = "액션 아이템 삭제 요청")
public record DeleteActionItemRequest(@NotNull
                                      @Schema(description = "액션 아이템 ID")
                                      Long actionItemId) {
}
