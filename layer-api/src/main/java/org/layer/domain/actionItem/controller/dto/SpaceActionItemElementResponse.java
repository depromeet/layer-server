package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.actionItem.entity.ActionItem;

@Builder
public record SpaceActionItemElementResponse(
                                    @NotNull
                                    @Schema(description = "행동 목표 ID")
                                    Long actionItemId,
                                    @NotNull
                                    @Schema(description = "행동 목표 내용")
                                    String content,
                                    @NotNull
                                    @Schema(description = "행동 목표와 매핑되는 회고 ID")
                                    Long retrospectId,
                                    @NotNull
                                    @Schema(description = "행동 목표와 매핑되는 회고 제목")
                                    String retrospectTitle) {

    public static SpaceActionItemElementResponse of(ActionItem actionItem, String retrospectName) {
        return SpaceActionItemElementResponse.builder()
                .actionItemId(actionItem.getId())
                .content(actionItem.getContent())
                .retrospectId(actionItem.getRetrospectId())
                .retrospectTitle(retrospectName)
                .build();
    }
}