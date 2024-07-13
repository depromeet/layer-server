package org.layer.domain.retrospect.service.dto.response;

import java.util.List;


public record RetrospectListGetServiceResponse(
	int layerCount,
	List<RetrospectGetServiceResponse> retrospects
) {
	public static RetrospectListGetServiceResponse of(int layerCount, List<RetrospectGetServiceResponse> retrospects){
		return new RetrospectListGetServiceResponse(layerCount, retrospects);
	}
}
