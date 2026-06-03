package org.layer.domain.reaction.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.reaction.entity.RetrospectReaction;

@Schema(name = "RetrospectReactionElementResponse", description = "회고 반응 요소 응답 DTO")
public record RetrospectReactionElementResponse(
        @Schema(description = "회고 반응 ID", example = "1")
        Long retrospectReactionId,

        @Schema(description = "반응 ID", example = "2")
        Long reactionId,

        @Schema(description = "반응한 멤버 ID", example = "3")
        Long memberId
) {
    public static RetrospectReactionElementResponse from(RetrospectReaction retrospectReaction) {
        return new RetrospectReactionElementResponse(
                retrospectReaction.getId(),
                retrospectReaction.getReactionId(),
                retrospectReaction.getMemberId()
        );
    }
}
