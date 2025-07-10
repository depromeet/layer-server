package org.layer.admin.space.controller.dto;

import org.layer.admin.space.enums.AdminSpaceCategory;

public record SpaceCountResponse(
	AdminSpaceCategory category,
	long spaceCount
) {
	public SpaceCountResponse(AdminSpaceCategory category, long spaceCount) {
		this.category = category;
		this.spaceCount = spaceCount;
	}
}
