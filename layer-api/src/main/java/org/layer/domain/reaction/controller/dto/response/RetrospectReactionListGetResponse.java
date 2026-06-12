package org.layer.domain.reaction.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "RetrospectReactionListGetResponse", description = "회고의 모든 답변에 대한 반응 조회 응답 DTO")
public record RetrospectReactionListGetResponse(
        @Schema(description = "답변별 반응 목록")
        List<AnswerReactionGetResponse> answerReactions
) {
    public static RetrospectReactionListGetResponse from(List<AnswerReactionGetResponse> answerReactions) {
        return new RetrospectReactionListGetResponse(answerReactions);
    }
}
