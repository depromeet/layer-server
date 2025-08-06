package org.layer.admin.template.controller.dto;

import org.layer.admin.template.enums.AdminChoiceType;

public record TemplateClickCountResponse(
	AdminChoiceType viewType,
	long count
) {
	public TemplateClickCountResponse(AdminChoiceType viewType, long count) {
		this.viewType = viewType;
		this.count = count;
	}
}
