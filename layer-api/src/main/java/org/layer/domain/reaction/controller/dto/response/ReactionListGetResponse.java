package org.layer.domain.reaction.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ReactionListGetResponse", description = "반응 목록 조회 응답 DTO")
public record ReactionListGetResponse(
        @Schema(description = "반응 목록")
        List<ReactionGetResponse> reactions
) {
    public static ReactionListGetResponse from(List<ReactionGetResponse> reactions) {
        return new ReactionListGetResponse(reactions);
    }
}
