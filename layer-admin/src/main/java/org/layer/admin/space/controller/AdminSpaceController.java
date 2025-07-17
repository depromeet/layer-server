package org.layer.admin.space.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.space.controller.dto.SpaceCountResponse;
import org.layer.admin.space.service.AdminSpaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminSpaceController {

	private final AdminSpaceService adminSpaceService;

	@GetMapping("/admin/space/individual-vs-team")
	public ResponseEntity<List<SpaceCountResponse>> getTemplateChoiceTotalCount(
		@RequestParam(name = "startDate") LocalDateTime startDate,
		@RequestParam(name = "endDate") LocalDateTime endDate) {

		List<SpaceCountResponse> response = adminSpaceService.getSpaceCount(startDate, endDate);
		return ResponseEntity.ok().body(response);
	}


}
