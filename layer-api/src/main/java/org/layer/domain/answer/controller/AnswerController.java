package org.layer.domain.answer.controller;

import org.layer.common.annotation.MemberId;
import org.layer.domain.answer.controller.dto.request.AnswerListCreateRequest;
import org.layer.domain.answer.controller.dto.response.AnswerListGetResponse;
import org.layer.domain.answer.controller.dto.response.TemporaryAnswerListResponse;
import org.layer.domain.answer.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

		answerService.create(request, spaceId, retrospectId, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Override
	@GetMapping("/temp")
	public ResponseEntity<TemporaryAnswerListResponse> getTemporaryAnswer(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId) {

		TemporaryAnswerListResponse dto = answerService.getTemporaryAnswer(spaceId, retrospectId, memberId);

		return ResponseEntity.ok().body(dto);
	}

	@Override
	@GetMapping("/analyze")
	public ResponseEntity<AnswerListGetResponse> getAnalyzeAnswer(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId) {
		AnswerListGetResponse response = answerService.getAnalyzeAnswer(spaceId, retrospectId, memberId);

		return ResponseEntity.ok().body(response);
	}
}
