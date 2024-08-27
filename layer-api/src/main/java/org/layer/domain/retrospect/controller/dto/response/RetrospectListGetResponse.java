package org.layer.domain.retrospect.controller.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RetrospectListGetResponse", description = "회고 목록 조회 Dto")
public record RetrospectListGetResponse(
	@Schema(description = "쌓인 회고 수", example = "3")
	int layerCount,
	@Schema(description = "회고 객체 목록", example = "")
	List<RetrospectGetResponse> retrospects

) {
	public static RetrospectListGetResponse of(int layerCount, List<RetrospectGetResponse> retrospects){
		return new RetrospectListGetResponse(layerCount, retrospects);
	}
}
