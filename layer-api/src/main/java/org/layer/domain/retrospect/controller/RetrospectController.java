package org.layer.domain.retrospect.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.layer.annotation.MemberId;
import org.layer.domain.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.domain.retrospect.controller.dto.request.RetrospectUpdateRequest;
import org.layer.domain.retrospect.controller.dto.response.RetrospectCreateResponse;
import org.layer.domain.retrospect.controller.dto.response.RetrospectListGetResponse;
import org.layer.domain.retrospect.service.RetrospectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/retrospect")
public class RetrospectController implements RetrospectApi {

	private final RetrospectService retrospectService;

	@Override
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<RetrospectCreateResponse> createRetrospect(
		@PathVariable("spaceId") Long spaceId,
		@RequestBody @Valid RetrospectCreateRequest request,
		@MemberId Long memberId) {

		var newRetrospectId = retrospectService.createRetrospect(request, spaceId, memberId);

		return ResponseEntity.ok().body(RetrospectCreateResponse.of(newRetrospectId));
	}

	@Override
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<RetrospectListGetResponse> getRetrospects(@PathVariable("spaceId") Long spaceId,
		@MemberId Long memberId) {

		return ResponseEntity.ok().body(retrospectService.getRetrospects(spaceId, memberId));
	}

	@Override
	@PatchMapping("/{retrospectId}")
	public ResponseEntity<RetrospectListGetResponse> updateRetrospect(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @RequestBody @Valid RetrospectUpdateRequest request,
		@MemberId Long memberId) {

		retrospectService.updateRetrospect(spaceId, retrospectId, request, memberId);
		return ResponseEntity.ok().build();
	}

	@Override
	@DeleteMapping("/{retrospectId}")
	public ResponseEntity<RetrospectListGetResponse> deleteRetrospect(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId) {

		retrospectService.deleteRetrospect(spaceId, retrospectId, memberId);
		return ResponseEntity.ok().build();
	}

	@Override
	@PatchMapping("/{retrospectId}/close")
	public ResponseEntity<Void> closeRetrospect(
		@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId,
		@MemberId Long memberId) {

		retrospectService.closeRetrospect(spaceId, retrospectId, memberId);

		return ResponseEntity.ok().build();
	}

}
