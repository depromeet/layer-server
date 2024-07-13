package org.layer.domain.retrospect.service.dto.request;

import java.util.List;

public record RetrospectCreateServiceRequest(
	String title,
	Long spaceId,
	List<String> questions,
	boolean isMyForm
) {
}
