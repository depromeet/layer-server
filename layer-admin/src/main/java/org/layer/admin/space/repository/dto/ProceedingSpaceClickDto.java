package org.layer.admin.space.repository.dto;

public record ProceedingSpaceClickDto(
	Long memberId,
	Long totalCount,
	Long proceedingCount
) {
	public ProceedingSpaceClickDto(Long memberId, Long totalCount, Long proceedingCount) {
		this.memberId = memberId;
		this.totalCount = totalCount;
		this.proceedingCount = proceedingCount;
	}
}
