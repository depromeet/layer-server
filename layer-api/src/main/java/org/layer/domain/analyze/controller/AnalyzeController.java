package org.layer.domain.analyze.controller;

import java.util.List;

import org.layer.common.annotation.MemberId;
import org.layer.domain.analyze.controller.dto.response.AnalyzeGetResponse;
import org.layer.domain.analyze.enums.AnalyzeType;
import org.layer.domain.analyze.service.AnalyzeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/retrospect/{retrospectId}/analyze")
public class AnalyzeController implements AnalyzeApi {

	private final AnalyzeService analyzeService;

	@Override
	@PostMapping
	public ResponseEntity<Void> createAnalyze(
		@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId,
		@MemberId Long memberId) {

		analyzeService.createAnalyze(spaceId, retrospectId, List.of(2L,4L));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Override
	@GetMapping
	public ResponseEntity<AnalyzeGetResponse> getAnalyze(
		@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId,
		@PathVariable("analyzeType") AnalyzeType analyzeType,
		@MemberId Long memberId) {

		AnalyzeGetResponse response = analyzeService.getAnalyze(spaceId, retrospectId, memberId, analyzeType);

		return ResponseEntity.ok().body(response);
	}
}
