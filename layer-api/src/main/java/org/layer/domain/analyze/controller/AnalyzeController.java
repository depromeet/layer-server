package org.layer.domain.analyze.controller;

import java.util.List;

import org.layer.common.annotation.MemberId;
import org.layer.domain.analyze.controller.dto.response.AnalyzesGetResponse;
import org.layer.domain.analyze.service.AnalyzeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		@RequestParam List<Long> memberIds) {

		analyzeService.createAnalyzeTemp(spaceId, retrospectId, memberIds);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Override
	@GetMapping
	public ResponseEntity<AnalyzesGetResponse> getAnalyze(
		@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId,
		@MemberId Long memberId) {

		AnalyzesGetResponse response = analyzeService.getAnalyze(spaceId, retrospectId, memberId);

		return ResponseEntity.ok().body(response);
	}
}
