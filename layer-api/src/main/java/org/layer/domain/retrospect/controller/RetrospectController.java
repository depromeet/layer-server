package org.layer.domain.retrospect.controller;

import java.util.List;

import org.layer.common.annotation.MemberId;
import org.layer.domain.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.domain.retrospect.controller.dto.response.RetrospectGetResponse;
import org.layer.domain.retrospect.controller.dto.response.RetrospectListGetResponse;
import org.layer.domain.retrospect.service.RetrospectService;
import org.layer.domain.retrospect.service.dto.request.RetrospectCreateServiceRequest;
import org.layer.domain.retrospect.service.dto.response.RetrospectListGetServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> createRetrospect(
		@PathVariable("spaceId") Long spaceId,
		@RequestBody @Valid RetrospectCreateRequest request,
		@MemberId Long memberId) {

		retrospectService.createRetrospect(request, spaceId, memberId);

		return ResponseEntity.ok().build();
	}

	@Override
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<RetrospectListGetResponse> getRetrospects(@PathVariable("spaceId") Long spaceId,
		@MemberId Long memberId) {

		RetrospectListGetServiceResponse serviceResponse = retrospectService.getRetrospects(spaceId, memberId);

		List<RetrospectGetResponse> retrospectGetResponses = serviceResponse.retrospects().stream()
			.map(r -> RetrospectGetResponse.of(r.retrospectId(), r.title(), r.introduction(), r.isWrite(), r.retrospectStatus(),
				r.writeCount(), r.totalCount()))
			.toList();

		return ResponseEntity.ok()
			.body(RetrospectListGetResponse.of(serviceResponse.layerCount(), retrospectGetResponses));
	}

}
