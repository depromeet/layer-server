package org.layer.admin.space.repository.dto;

public record ProceedingSpaceDto(
	Long spaceId,
	Long memberCount
) {
	public ProceedingSpaceDto(Long spaceId, Long memberCount) {
		this.spaceId = spaceId;
		this.memberCount = memberCount;
	}
}
