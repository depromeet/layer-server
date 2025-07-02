package org.layer.admin.template.controller.dto;

import org.layer.admin.template.enums.AdminViewType;

public record TemplateViewCountResponse(
	AdminViewType viewType,
	long count
) {
	public TemplateViewCountResponse(AdminViewType viewType, long count) {
		this.viewType = viewType;
		this.count = count;
	}
}
