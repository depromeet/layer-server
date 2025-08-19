package org.layer.admin.space.repository.dto;

public record ProceedingSpaceImpressionDto(
	Long memberId,
	Long totalCount
) {
	public ProceedingSpaceImpressionDto(Long memberId, Long totalCount) {
		this.memberId = memberId;
		this.totalCount = totalCount;
	}
}
