package org.layer.domain.actionItem.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "실행 목표 생성 요청 dto")
public record ActionItemCreateBySpaceIdRequest(@NotNull
                                      @Schema(description = "액션 아이템과 매핑되는 회고 ID")
                                      Long spaceId,
                                      @NotNull
                                      @Schema(description = "액션 아이템 내용")
                                      @NotNull String content) {
}