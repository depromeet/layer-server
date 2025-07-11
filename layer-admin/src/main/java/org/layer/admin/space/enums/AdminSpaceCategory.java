package org.layer.admin.space.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdminSpaceCategory {
	INDIVIDUAL("INDIVIDUAL"),
	TEAM("TEAM");

	private final String category;

	public static AdminSpaceCategory from(String category) {
		for (AdminSpaceCategory c : values()) {
			if (c.getCategory().equalsIgnoreCase(category)) {
				return c;
			}
		}
		throw new IllegalArgumentException("Unknown tag: " + category);
	}
}
