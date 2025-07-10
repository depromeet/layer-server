package org.layer.admin.template.controller.dto;

import org.layer.admin.template.enums.AdminFormTag;

public record TemplateChoiceCountResponse(
	AdminFormTag formTag,
	long recommendedCount
) {
	public TemplateChoiceCountResponse(AdminFormTag formTag, long recommendedCount) {
		this.formTag = formTag;
		this.recommendedCount = recommendedCount;
	}
}
