package org.layer.domain.retrospect.controller;

import org.layer.common.annotation.MemberId;
import org.layer.domain.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.domain.retrospect.controller.dto.response.RetrospectListGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "회고", description = "회고 관련 API")
public interface RetrospectApi {

	@Operation(summary = "회고 생성", description = "")
	ResponseEntity<Void> createRetrospect(@PathVariable("spaceId") Long spaceId,
		@RequestBody @Valid RetrospectCreateRequest request, @MemberId Long memberId);

	@Operation(summary = "회고 목록 조회", description = "특정 팀 스페이스에서 작성했던 회고 목록을 보는 기능입니다.")
	ResponseEntity<RetrospectListGetResponse> getRetrospects(@PathVariable("spaceId") Long spaceId, @MemberId Long memberId);
}
