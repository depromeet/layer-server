package org.layer.retrospect.service.dto;

import java.util.List;

public record RetrospectCreateServiceRequest(
	String title,
	Long spaceId,
	List<String> questions,
	boolean isMyForm
) {
}
