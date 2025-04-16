package org.layer.domain.form.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.layer.annotation.MemberId;
import org.layer.domain.form.controller.dto.request.FormNameUpdateRequest;
import org.layer.domain.form.controller.dto.request.RecommendFormQueryDto;
import org.layer.domain.form.controller.dto.request.RecommendFormSetRequest;
import org.layer.domain.form.controller.dto.response.CustomTemplateListResponse;
import org.layer.domain.form.controller.dto.response.FormGetResponse;
import org.layer.domain.form.controller.dto.response.RecommendFormResponseDto;
import org.layer.domain.form.service.FormService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

		RecommendFormResponseDto response = formService.getRecommendTemplate(queryDto);
		return ResponseEntity.ok().body(response);
	}

	@Override
	@PostMapping("/recommend")
	public ResponseEntity<Void> setRecommendTemplate(@RequestBody @Valid RecommendFormSetRequest request,
		@MemberId Long memberId) {

		formService.setRecommendTemplate(request, memberId);
		return ResponseEntity.ok().build();
	}

	@Override
	@PatchMapping("/{formId}/title")
	public ResponseEntity<Void> updateFormTitle(@PathVariable Long formId,
		@RequestBody @Valid FormNameUpdateRequest request, @MemberId Long memberId) {
		formService.updateFormTitle(formId, request, memberId);

		return ResponseEntity.ok().build();
	}

	@Override
	@DeleteMapping("/{formId}")
	public ResponseEntity<Void> deleteFormTitle(@PathVariable Long formId, @MemberId Long memberId) {

		formService.deleteFormTitle(formId, memberId);

		return ResponseEntity.ok().build();
	}

	@Override
	@GetMapping("/space/{spaceId}/custom-template")
	public ResponseEntity<CustomTemplateListResponse> getCustomTemplateList(
		@PageableDefault(size = 10) Pageable pageable, @PathVariable(name = "spaceId") Long spaceId,
		@MemberId Long memberId) {

		CustomTemplateListResponse response = formService.getCustomTemplateList(pageable, spaceId, memberId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
