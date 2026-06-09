package org.layer.domain.actionItem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.actionItem.entity.ActionItem;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ActionItemResponse {
    @NotNull
    Long actionItemId;

    @NotNull
    String content;

    LocalDateTime createdAt;

    public ActionItemResponse(Long actionItemId, String content, LocalDateTime createdAt) {
        this.actionItemId = actionItemId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static ActionItemResponse of(ActionItem actionItem) {
        return ActionItemResponse.builder()
                .actionItemId(actionItem.getId())
                .content(actionItem.getContent())
                .createdAt(actionItem.getCreatedAt())
                .build();
    }
}
