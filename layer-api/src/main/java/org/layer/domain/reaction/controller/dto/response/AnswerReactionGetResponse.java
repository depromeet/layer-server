package org.layer.domain.reaction.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "AnswerReactionGetResponse", description = "특정 답변의 회고 반응 조회 응답 DTO")
public record AnswerReactionGetResponse(
        @Schema(description = "답변 ID", example = "1")
        Long answerId,

        @Schema(description = "해당 답변의 반응 목록")
        List<RetrospectReactionElementResponse> reactions
) {
    public static AnswerReactionGetResponse of(Long answerId, List<RetrospectReactionElementResponse> reactions) {
        return new AnswerReactionGetResponse(answerId, reactions);
    }
}
