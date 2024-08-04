package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.space.entity.Space;

@Builder
public record MemberActionItemElementResponse(
                                            @NotNull
                                            @Schema(description = "실행 목표가 속한 스페이스 ID")
                                            Long spaceId,
                                            @NotNull
                                            @Schema(description = "실행 목표가 속한 스페이스 이름")
                                            String spaceName,
                                            @NotNull
                                            @Schema(description = "실행 목표가 속한 회고 ID")
                                            Long retrospectId,
                                            @NotNull
                                            @Schema(description = "실행 목표가 속한 회고 이름")
                                            String retrospectTitle,
                                            @NotNull
                                            @Schema(description = "실행 목표 ID", examples = {"1", "2"})
                                            Long actionItemId,
                                            @NotNull
                                            @Schema(description = "실행 목표 내용", examples = {"긴 회의 시간 줄이기", "회의 후 내용 꼭 기록해두기"})
                                            String content
) {

    public static MemberActionItemElementResponse of(Space space, Retrospect retrospect, ActionItem actionItem) {
        return MemberActionItemElementResponse.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .retrospectId(retrospect.getId())
                .retrospectTitle(retrospect.getTitle())
                .actionItemId(actionItem.getId())
                .content(actionItem.getContent())
                .build();
    }
}