package org.layer.admin.retrospect.repository.dto;

public record ProceedingRetrospectClickDto(
	Long memberId,
	Long totalCount,
	Long proceedingCount
) {
	public ProceedingRetrospectClickDto(Long memberId, Long totalCount, Long proceedingCount) {
		this.memberId = memberId;
		this.totalCount = totalCount;
		this.proceedingCount = proceedingCount;
	}
}
