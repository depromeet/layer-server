package org.layer.domain.space.dto;

import lombok.Getter;

@Getter
public class AdminSpaceGetResponse {

	private final String spaceName;
	private final String spaceLeaderName;

	public AdminSpaceGetResponse(String spaceName, String spaceLeaderName) {
		this.spaceName = spaceName;
		this.spaceLeaderName = spaceLeaderName;
	}
}
