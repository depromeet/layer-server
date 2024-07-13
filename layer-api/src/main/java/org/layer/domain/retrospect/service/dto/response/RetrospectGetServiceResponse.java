package org.layer.domain.retrospect.service.dto.response;

import org.layer.domain.retrospect.entity.RetrospectStatus;

public record RetrospectGetServiceResponse(
	String title,
	String introduction,
	boolean isWrite,
	RetrospectStatus retrospectStatus,
	int writeCount
) {
	public static RetrospectGetServiceResponse of(String title, String introduction, boolean isWrite,
		RetrospectStatus retrospectStatus, int writeCount){
		return new RetrospectGetServiceResponse(title, introduction, isWrite, retrospectStatus, writeCount);
	}
}
