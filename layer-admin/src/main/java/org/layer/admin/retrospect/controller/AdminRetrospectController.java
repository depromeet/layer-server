package org.layer.admin.retrospect.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.retrospect.controller.dto.MeaningfulRetrospectMemberResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectRetentionResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectStayTimeResponse;
import org.layer.admin.retrospect.service.AdminRetrospectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminRetrospectController {

	private final AdminRetrospectService adminRetrospectService;

	@GetMapping("/admin/retrospect/stay-time")
	public ResponseEntity<List<RetrospectStayTimeResponse>> getAllRetrospectStayTime(
		@RequestParam(name = "startDate") LocalDateTime startDate,
		@RequestParam(name = "endDate") LocalDateTime endDate) {

		List<RetrospectStayTimeResponse> responses = adminRetrospectService.getAllRetrospectStayTime(
			startDate, endDate);

		return ResponseEntity.ok().body(responses);
	}

	@GetMapping("/admin/retrospect/meaningful")
	public ResponseEntity<MeaningfulRetrospectMemberResponse> getAllMeaningfulRetrospect(
		@RequestParam(name = "startDate") LocalDateTime startDate,
		@RequestParam(name = "endDate") LocalDateTime endDate,
		@RequestParam(name = "retrospectLength") int retrospectLength,
		@RequestParam(name = "retrospectCount") int retrospectCount) {

		MeaningfulRetrospectMemberResponse response = adminRetrospectService.getAllMeaningfulRetrospect(
			startDate, endDate, retrospectLength, retrospectCount);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/admin/retrospect/retention")
	public RetrospectRetentionResponse getRetrospectRetention(
		@RequestParam(name = "startDate") LocalDateTime startDate,
		@RequestParam(name = "endDate") LocalDateTime endDate) {

		return adminRetrospectService.getRetrospectRetention(startDate, endDate);
	}
}
