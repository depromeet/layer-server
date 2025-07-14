package org.layer.admin.retrospect.controller;

import java.time.LocalDateTime;
import java.util.List;

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
}
