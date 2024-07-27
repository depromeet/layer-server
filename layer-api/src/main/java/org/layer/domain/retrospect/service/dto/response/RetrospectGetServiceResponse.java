package org.layer.domain.retrospect.service.dto.response;

import org.layer.domain.retrospect.entity.RetrospectStatus;

public record RetrospectGetServiceResponse(
	Long retrospectId,
	String title,
	String introduction,
	boolean isWrite,
	RetrospectStatus retrospectStatus,
	long writeCount,
	long totalCount
) {
	public static RetrospectGetServiceResponse of(Long retrospectId, String title, String introduction, boolean isWrite,
		RetrospectStatus retrospectStatus, long writeCount, long totalCount) {
		return new RetrospectGetServiceResponse(retrospectId, title, introduction, isWrite, retrospectStatus,
			writeCount, totalCount);
	}
}
