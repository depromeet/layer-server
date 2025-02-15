package org.layer.domain.admin.retrospect.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.layer.domain.retrospect.dto.AdminRetrospectGetResponse;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class AdminRetrospectsGetResponse {

	private final List<AdminRetrospectGetResponse> retrospects;
	private final Integer totalCount;

}
