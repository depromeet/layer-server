package org.layer.domain.question.controller;

import org.layer.annotation.MemberId;
import org.layer.domain.question.controller.dto.response.QuestionListGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "질문", description = "질문 관련 API")
public interface QuestionApi {
	@Operation(summary = "특정 회고 질문 목록 조회", description = "")
	ResponseEntity<QuestionListGetResponse> getRetrospectQuestions(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId);
}
