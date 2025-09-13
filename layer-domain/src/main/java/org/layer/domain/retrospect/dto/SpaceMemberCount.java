package org.layer.domain.retrospect.dto;

import lombok.Getter;

@Getter
public class SpaceMemberCount {
	private final Long spaceId;
	private final Long memberCount;

	public SpaceMemberCount(Long spaceId, Long memberCount) {
		this.spaceId = spaceId;
		this.memberCount = memberCount;
	}
}
