package org.layer.domain.answer.controller;

import java.util.List;

import org.layer.common.annotation.MemberId;
import org.layer.domain.answer.controller.dto.request.AnswerListCreateRequest;
import org.layer.domain.answer.service.AnswerService;
import org.layer.domain.answer.service.dto.request.AnswerCreateServiceRequest;
import org.layer.domain.answer.service.dto.request.AnswerListCreateServiceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/retrospect/{retrospectId}/answer")
public class AnswerController implements AnswerApi {
	private final AnswerService answerService;

	@Override
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> createAnswer(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId,
		@RequestBody @Valid AnswerListCreateRequest request, @MemberId Long memberId) {

		List<AnswerCreateServiceRequest> requests = request.requests().stream()
			.map(r -> AnswerCreateServiceRequest.of(r.questionId(), r.questionType(), r.answer()))
			.toList();

		answerService.create(AnswerListCreateServiceRequest.of(requests), spaceId, retrospectId, memberId);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
