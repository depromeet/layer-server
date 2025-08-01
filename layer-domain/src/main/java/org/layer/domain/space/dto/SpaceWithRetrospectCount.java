package org.layer.domain.space.dto;

import lombok.Getter;

@Getter
public class SpaceWithRetrospectCount {
	private final Long id;
	private final Long retrospectCount;
	private final Long proceedingRetrospectCount;

	public SpaceWithRetrospectCount(Long id, Long retrospectCount, Long proceedingRetrospectCount) {
		this.id = id;
		this.retrospectCount = retrospectCount;
		this.proceedingRetrospectCount = proceedingRetrospectCount;
	}
}

