package org.layer.admin.retrospect.controller.dto;

public record RetrospectOverviewResponse(
	long createdRetrospectCount,
	long completedRetrospectCount,
	double averageCompletionRate,
	double averageRetrospectLength,
	double averageWritingTimeMinutes
) {
}
