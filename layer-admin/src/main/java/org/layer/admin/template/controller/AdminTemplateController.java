package org.layer.admin.template.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.template.controller.dto.TemplateChoiceCountResponse;
import org.layer.admin.template.controller.dto.TemplateViewCountResponse;
import org.layer.admin.template.enums.AdminChoiceType;
import org.layer.admin.template.service.AdminTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminTemplateController {
	private final AdminTemplateService adminTemplateService;

	@GetMapping("/admin/template/choice-count")
	public ResponseEntity<List<TemplateChoiceCountResponse>> getTemplateChoiceTotalCount(
		@RequestParam(name = "startDate") LocalDateTime startDate,
		@RequestParam(name = "endDate") LocalDateTime endDate,
		@RequestParam(name = "choiceType", required = false) AdminChoiceType choiceType) {

		List<TemplateChoiceCountResponse> response = adminTemplateService.getTemplateChoiceCount(startDate,
			endDate, choiceType);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/admin/template/recommended-list-count")
	public ResponseEntity<List<TemplateViewCountResponse>> getTemplateRecommendedListCount(
		@RequestParam(name = "startDate") LocalDateTime startDate,
		@RequestParam(name = "endDate") LocalDateTime endDate
	) {
		List<TemplateViewCountResponse> responses = adminTemplateService.getTemplateRecommendedListCount(
			startDate, endDate);

		return ResponseEntity.ok().body(responses);
	}

}
