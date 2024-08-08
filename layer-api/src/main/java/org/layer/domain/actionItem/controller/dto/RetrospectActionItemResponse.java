package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record RetrospectActionItemResponse(@NotNull
                                            @Schema(description = "회고 ID", example = "1")
                                            Long retrospectId,
                                            @NotNull
                                            @Schema(description = "회고 제목", example = "중간 발표 이후 회고")
                                            String retrospectTitle,
                                            @NotNull
                                            @Schema(description = "액션 아이템 아이디와 내용 리스트")
                                            List<ActionItemResponse> actionItemList) {
}
