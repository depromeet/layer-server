package org.layer.retrospect.controller;

import org.layer.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/retrospect")
public class RetrospectController implements RetrospectApi {

	@Override
	@PostMapping
	public ResponseEntity<Void> createRetrospect(@PathVariable("spaceId") Long spaceId,
		@RequestBody @Valid RetrospectCreateRequest request) {

		return ResponseEntity.ok(null);
	}
}
