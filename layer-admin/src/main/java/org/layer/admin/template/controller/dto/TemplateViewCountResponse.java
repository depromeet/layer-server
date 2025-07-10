package org.layer.admin.template.controller.dto;

import org.layer.admin.template.enums.AdminChoiceType;

public record TemplateViewCountResponse(
	AdminChoiceType viewType,
	long count
) {
	public TemplateViewCountResponse(AdminChoiceType viewType, long count) {
		this.viewType = viewType;
		this.count = count;
	}
}
