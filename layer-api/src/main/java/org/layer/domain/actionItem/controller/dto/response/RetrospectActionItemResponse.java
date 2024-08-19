package org.layer.domain.actionItem.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.actionItem.dto.ActionItemResponse;
import org.layer.domain.actionItem.enums.ActionItemStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RetrospectActionItemResponse(@NotNull
                                            @Schema(description = "회고 ID", example = "1")
                                            Long retrospectId,
                                            @NotNull
                                            @Schema(description = "회고 제목", example = "중간 발표 이후 회고")
                                            String retrospectTitle,
                                            @NotNull
                                            @Schema(description = "회고 마감일")
                                            LocalDateTime deadline,
                                            @NotNull
                                            @Schema(description = "실행 목표 상태")
                                            ActionItemStatus status,
                                            @NotNull
                                            @Schema(description = "액션 아이템 아이디와 내용 리스트")
                                            List<ActionItemResponse> actionItemList) {
}
