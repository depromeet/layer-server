package org.layer.domain.question.controller;

import java.util.List;

import org.layer.common.annotation.MemberId;
import org.layer.domain.question.controller.dto.response.QuestionGetResponse;
import org.layer.domain.question.controller.dto.response.QuestionListGetResponse;
import org.layer.domain.question.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/retrospect/{retrospectId}/question")
public class QuestionController implements QuestionApi{
	private final QuestionService questionService;

	@Override
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<QuestionListGetResponse> getRetrospectQuestions(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId) {

		List<QuestionGetResponse> responses = questionService.getRetrospectQuestions(spaceId, retrospectId, memberId)
			.questions()
			.stream()
			.map(q -> QuestionGetResponse.of(q.question(), q.order(), q.questionType()))
			.toList();

		return ResponseEntity.ok().body(QuestionListGetResponse.of(responses));
	}
}
