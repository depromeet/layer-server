package org.layer.admin.template.controller;

import java.util.List;

import org.layer.admin.template.controller.dto.TemplateRecommendedCountResponse;
import org.layer.admin.template.service.AdminTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminTemplateController {
	private final AdminTemplateService adminTemplateService;

	@GetMapping("/admin/template/recommended-count")
	public ResponseEntity<List<TemplateRecommendedCountResponse>> getTemplateRecommendedCount() {

		List<TemplateRecommendedCountResponse> response = adminTemplateService.getTemplateRecommendedCount();
		return ResponseEntity.ok().body(response);
	}
}
