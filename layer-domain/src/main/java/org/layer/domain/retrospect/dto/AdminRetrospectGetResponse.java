package org.layer.domain.retrospect.dto;

import lombok.Getter;

@Getter
public class AdminRetrospectGetResponse {
	private final String retrospectTitle;
	private final String retrospectCreator;

	public AdminRetrospectGetResponse(String retrospectTitle, String retrospectCreator) {
		this.retrospectTitle = retrospectTitle;
		this.retrospectCreator = retrospectCreator;
	}
}
