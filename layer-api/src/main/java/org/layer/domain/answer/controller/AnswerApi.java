package org.layer.domain.answer.controller;

import org.layer.common.annotation.MemberId;
import org.layer.domain.answer.controller.dto.request.AnswerListCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "회고 작성", description = "회고 작성 관련 API")
public interface AnswerApi {

	@Operation(summary = "회고 작성", description = "")
	ResponseEntity<Void> createAnswer(@PathVariable("spaceId") Long spaceId, @PathVariable("retrospectId") Long retrospectId,
		@RequestBody @Valid AnswerListCreateRequest request, @MemberId Long memberId);
}
