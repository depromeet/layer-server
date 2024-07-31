package org.layer.domain.answer.controller.dto.response;

import java.util.List;

import org.layer.domain.answer.controller.dto.response.TemporaryAnswerGetResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TemporaryAnswerListResponse", description = "임시 회고 조회 응답 Dto")
public record TemporaryAnswerListResponse(
	List<TemporaryAnswerGetResponse> answers
) {
	public static TemporaryAnswerListResponse of(List<TemporaryAnswerGetResponse> answers){
		return new TemporaryAnswerListResponse(answers);
	}
}
