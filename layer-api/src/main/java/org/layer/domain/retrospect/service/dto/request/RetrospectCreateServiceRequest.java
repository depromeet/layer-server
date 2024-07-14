package org.layer.domain.retrospect.service.dto.request;

import java.util.List;

public record RetrospectCreateServiceRequest(
	String title,
	String introduction,
	Long spaceId,
	List<String> questions
) {
	public static RetrospectCreateServiceRequest of(String title, String introduction, Long spaceId, List<String> questions) {

		return new RetrospectCreateServiceRequest(title, introduction, spaceId, questions);
	}
}
