package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.actionItem.entity.ActionItem;

@Builder
public record ActionItemResponse(@NotNull
                                 @Schema(description = "실행 목표 ID", examples = {"1", "2"})
                                 Long actionItemId,
                                 @NotNull
                                 @Schema(description = "실행 목표 내용", examples = {"긴 회의 시간 줄이기", "회의 후 내용 꼭 기록해두기"})
                                 String content) {
    public static ActionItemResponse of(ActionItem actionItem) {
        return ActionItemResponse.builder()
                .actionItemId(actionItem.getId())
                .content(actionItem.getContent())
                .build();
    }
}
