package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.space.entity.Space;

import java.util.List;

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
                                            @Schema(description = "회고의 실행 목표 리스트")
                                            List<ActionItemResponse> teamActionItemList
) {

    public static MemberActionItemElementResponse of(Space space, Retrospect retrospect, List<ActionItemResponse> actionItemList) {
        return MemberActionItemElementResponse.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .retrospectId(retrospect.getId())
                .retrospectTitle(retrospect.getTitle())
                .teamActionItemList(actionItemList)
                .build();
    }
}