package org.layer.admin.retrospect.repository.dto;

public record ProceedingRetrospectImpressionDto(
	Long memberId,
	Long totalCount
) {
	public ProceedingRetrospectImpressionDto(Long memberId, Long totalCount) {
		this.memberId = memberId;
		this.totalCount = totalCount;
	}
}
