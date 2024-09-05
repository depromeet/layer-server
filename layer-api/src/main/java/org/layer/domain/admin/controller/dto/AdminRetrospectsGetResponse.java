package org.layer.domain.admin.controller.dto;

import java.util.List;

import org.layer.domain.retrospect.dto.AdminRetrospectGetResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AdminRetrospectsGetResponse {

	private final List<AdminRetrospectGetResponse> retrospects;
	private final Integer totalCount;

}
