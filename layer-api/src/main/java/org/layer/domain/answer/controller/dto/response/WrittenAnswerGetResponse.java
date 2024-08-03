package org.layer.domain.answer.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "종료되지 않은 작성완료 된 답변 응답 Dto")
public record WrittenAnswerGetResponse(
        @Schema(description = "질문 아이디")
        Long questionId,
        @Schema(description = "질문 타입", example = "plain_text")
        String questionType,

        @Schema(description = "질문 내용", example = "계속 유지하고 싶은 것은 무엇인가요?")
        String questionContent,

        @Schema(description = "답변")
        String answerContent) {
    public static WrittenAnswerGetResponse of(Long questionId, String questionType, String questionContent, String answerContent) {
        return new WrittenAnswerGetResponse(questionId, questionType, questionContent, answerContent);
    }

}
