package org.layer.domain.form.controller;

import org.layer.common.annotation.MemberId;
import org.layer.domain.form.controller.dto.request.FormNameUpdateRequest;
import org.layer.domain.form.controller.dto.request.RecommendFormQueryDto;
import org.layer.domain.form.controller.dto.response.CustomTemplateListResponse;
import org.layer.domain.form.controller.dto.response.FormGetResponse;
import org.layer.domain.form.controller.dto.response.RecommendFormResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "회고 폼(커스텀 템플릿)", description = "회고 폼 관련 API")
public interface FormApi {

	@Operation(summary = "회고 폼(커스텀 템플릿 포함) 질문 조회", method = "GET", description = "회고 폼(커스텀 템플릿)을 조회하는 기능입니다.")
	ResponseEntity<FormGetResponse> getForm(@PathVariable Long formId, @MemberId Long memberId);

	@Parameters(
		{
			@Parameter(name = "periodic", description = "회고 주기적 여부", example = "true", required = true),
			@Parameter(name = "period", description = "회고 주기", example = "WEEKLY"),
			@Parameter(name = "purpose", description = "회고 목적", example = "TEAM_GROWTH", required = true)
		}
	)
	@Operation(summary = "추천 템플릿 조회", method = "GET", description = "추천 템플릿을 조회하는 기능입니다.")
	ResponseEntity<RecommendFormResponseDto> getRecommendTemplate(@ModelAttribute @Valid @Parameter(hidden = true) RecommendFormQueryDto queryDto,
		@MemberId Long memberId);

	@Operation(summary = "커스텀 템플릿 제목 수정", method = "PATCH", description = "커스텀 템플릿 제목을 수정합니다.")
	ResponseEntity<Void> updateFormTitle(@PathVariable Long formId, @RequestBody @Valid FormNameUpdateRequest request, @MemberId Long memberId);

	@Operation(summary = "커스텀 템플릿 삭제", method = "DELETE", description = "커스텀 템플릿을 삭제합니다.")
	ResponseEntity<Void> deleteFormTitle(@PathVariable Long formId, @MemberId Long memberId);

	@Operation(summary = "스페이스에 속한 커스텀 템플릿 목록 조회", method = "GET", description = "스페이스의 커스텀 템플릿을 모두 조회합니다. (스페이스에 속한 팀원이라면 조회 가능)")
	ResponseEntity<CustomTemplateListResponse> getCustomTemplateList(Pageable pageable, @PathVariable Long spaceId, @MemberId Long memberId);
}
