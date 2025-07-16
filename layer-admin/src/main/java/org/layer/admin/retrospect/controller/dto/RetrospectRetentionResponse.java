package org.layer.admin.retrospect.controller.dto;

public record RetrospectRetentionResponse(
	long retrospectRetentionPeriodSeconds,
	long retrospectCreateCount,
	long totalMemberCount
) {
	public static RetrospectRetentionResponse of(
		long retrospectRetentionPeriodSeconds,
		long retrospectCreateCount,
		long totalMemberCount
	) {
		return new RetrospectRetentionResponse(
			retrospectRetentionPeriodSeconds, retrospectCreateCount, totalMemberCount
		);
	}
}
