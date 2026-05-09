package org.layer.admin.retrospect.controller.dto;

import java.util.List;

public record RetrospectCreationCycleResponse(
	double overallAverageDays,
	double teamAverageDays,
	double individualAverageDays,
	List<CycleDistributionEntry> distribution
) {
}
