package org.layer.retrospect.controller;

import org.layer.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.retrospect.service.RetrospectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/retrospect")
public class RetrospectController implements RetrospectApi {

	private final RetrospectService retrospectService;

	@Override
	@PostMapping
	public ResponseEntity<Void> createRetrospect(@PathVariable("spaceId") Long spaceId,
		@RequestBody @Valid RetrospectCreateRequest request) {

		retrospectService.create(spaceId, request.formId(), request.title(), request.introduction());
		return ResponseEntity.ok(null);
	}
}
