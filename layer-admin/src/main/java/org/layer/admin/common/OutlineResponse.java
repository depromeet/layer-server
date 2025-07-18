package org.layer.admin.common;

public record OutlineResponse(
	long totalMemberCount,
	long totalSpaceCount,
	long totalRetrospectCount,
	long totalRetrospectAnswerCount
) {
}
