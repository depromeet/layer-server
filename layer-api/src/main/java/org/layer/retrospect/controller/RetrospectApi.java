package org.layer.retrospect.controller;

import org.layer.retrospect.controller.dto.request.RetrospectCreateRequest;
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
		@RequestBody @Valid RetrospectCreateRequest request);
}
