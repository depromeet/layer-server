package org.layer.admin.retrospect.controller.dto;

public record RetrospectCreationCycleAverageResponse(
	double totalAverageSeconds,
	double teamRetrospectAverageSeconds,
	double individualRetrospectAverageSeconds
) {
}
