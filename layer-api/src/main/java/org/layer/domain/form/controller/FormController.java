package org.layer.domain.form.controller;

import org.layer.common.annotation.MemberId;
import org.layer.domain.form.controller.dto.request.RecommendFormQueryDto;
import org.layer.domain.form.controller.dto.response.FormGetResponse;
import org.layer.domain.form.controller.dto.response.RecommendFormResponseDto;
import org.layer.domain.form.service.FormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/form")
public class FormController implements FormApi {
	private final FormService formService;

	@Override
	@GetMapping("/{formId}")
	public ResponseEntity<FormGetResponse> getForm(@PathVariable Long formId, @MemberId Long memberId) {
		FormGetResponse dto = formService.getForm(formId, memberId);

		return ResponseEntity.ok().body(dto);
	}

	@Override
	@GetMapping("/recommend")
	public ResponseEntity<RecommendFormResponseDto> getRecommendTemplate(
		@Valid @ModelAttribute RecommendFormQueryDto queryDto,
		@MemberId Long memberId) {

		RecommendFormResponseDto response = formService.getRecommendTemplate(queryDto, memberId);
		return ResponseEntity.ok().body(response);
	}

}
