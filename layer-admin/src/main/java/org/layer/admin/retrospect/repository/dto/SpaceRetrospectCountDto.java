package org.layer.admin.retrospect.repository.dto;

public record SpaceRetrospectCountDto(
	Long spaceId,
	long count
) {
	public SpaceRetrospectCountDto(Long spaceId, long count) {
		this.spaceId = spaceId;
		this.count = count;
	}
}
