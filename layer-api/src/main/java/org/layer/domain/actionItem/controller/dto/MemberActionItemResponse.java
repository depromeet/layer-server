package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.space.entity.Space;

import java.util.List;

@Builder
public record MemberActionItemResponse(@NotNull
                                       @Schema(description = "회고 ID", example = "1")
                                       Long retrospectId,
                                       @NotNull
                                       @Schema(description = "회고 제목", example = "중간 발표 이후 회고")
                                       String retrospectTitle,
                                       @NotNull
                                       @Schema(description = "회고가 속한 스페이스 ID", example = "1")
                                       Long spaceId,
                                       @NotNull
                                       @Schema(description = "회고가 속한 스페이스 이름", example = "떡잎방범대")
                                       String spaceName,
                                       @NotNull
                                       @Schema(description = "액션 아이템 아이디와 내용 리스트")
                                       List<ActionItemResponse> actionItemList) {
    public static MemberActionItemResponse of(Space space, Retrospect retrospect, List<ActionItemResponse> actionItemList) {
        return MemberActionItemResponse.builder()
                .retrospectId(retrospect.getId())
                .retrospectTitle(retrospect.getTitle())
                .spaceId(space.getId())
                .spaceName(space.getName())
                .actionItemList(actionItemList)
                .build();
    }
}
