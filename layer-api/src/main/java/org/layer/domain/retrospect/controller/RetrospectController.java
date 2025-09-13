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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RetrospectController implements RetrospectApi {

	private final RetrospectService retrospectService;

	@Override
	@PostMapping("/space/{spaceId}/retrospect")
	public ResponseEntity<RetrospectCreateResponse> createRetrospect(
		@PathVariable("spaceId") Long spaceId,
		@RequestBody @Valid RetrospectCreateRequest request,
		@MemberId Long memberId) {

		var newRetrospectId = retrospectService.createRetrospect(request, spaceId, memberId);

		return ResponseEntity.ok().body(RetrospectCreateResponse.of(newRetrospectId));
	}

	@Override
	@GetMapping("/space/{spaceId}/retrospect")
	public ResponseEntity<RetrospectListGetResponse> getRetrospects(@PathVariable("spaceId") Long spaceId,
		@MemberId Long memberId) {

		return ResponseEntity.ok().body(retrospectService.getRetrospects(spaceId, memberId));
	}

	@Override
	@GetMapping("/retrospects")
	public ResponseEntity<RetrospectListGetResponse> getAllRetrospects(@MemberId Long memberId) {

		return ResponseEntity.ok().body(retrospectService.getAllRetrospects(memberId));
	}

	@Override
	@PatchMapping("/space/{spaceId}/retrospect/{retrospectId}")
	public ResponseEntity<RetrospectListGetResponse> updateRetrospect(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @RequestBody @Valid RetrospectUpdateRequest request,
		@MemberId Long memberId) {

		retrospectService.updateRetrospect(spaceId, retrospectId, request, memberId);
		return ResponseEntity.ok().build();
	}

	@Override
	@DeleteMapping("/space/{spaceId}/retrospect/{retrospectId}")
	public ResponseEntity<RetrospectListGetResponse> deleteRetrospect(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId) {

		retrospectService.deleteRetrospect(spaceId, retrospectId, memberId);
		return ResponseEntity.ok().build();
	}

	@Override
	@PatchMapping("/space/{spaceId}/retrospect/{retrospectId}/close")
	public ResponseEntity<Void> closeRetrospect(
		@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId,
		@MemberId Long memberId) {

		retrospectService.closeRetrospect(spaceId, retrospectId, memberId);

		return ResponseEntity.ok().build();
	}

}
