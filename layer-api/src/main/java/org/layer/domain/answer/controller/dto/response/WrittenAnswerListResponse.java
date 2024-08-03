package org.layer.domain.answer.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "작성된 회고 조회 응답 Dto")
public record WrittenAnswerListResponse(
        @Schema
        List<WrittenAnswerGetResponse> answers
) {
    public static WrittenAnswerListResponse of(List<WrittenAnswerGetResponse> answers) {
        return new WrittenAnswerListResponse(answers);
    }
}

