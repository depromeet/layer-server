package org.layer.domain.form.controller;

import org.layer.common.annotation.MemberId;
import org.layer.domain.form.controller.dto.response.DefaultFormGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회고 폼(커스텀 템플릿)", description = "회고 폼 관련 API")
public interface FormApi {

	@Operation(summary = "회고 폼(커스텀 템플릿) 조회 (기본 회고폼 포함)", method = "GET", description = "회고 폼(커스텀 템플릿)을 조회하는 기능입니다.")
	ResponseEntity<DefaultFormGetResponse> getForm(@PathVariable Long formId, @MemberId Long memberId);

}
