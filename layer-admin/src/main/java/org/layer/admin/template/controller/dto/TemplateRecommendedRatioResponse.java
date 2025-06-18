package org.layer.admin.template.controller.dto;

import org.layer.admin.template.enums.AdminFormTag;

public record TemplateRecommendedRatioResponse(
	AdminFormTag formTag,
	long recommendedCount
) {
	public TemplateRecommendedRatioResponse(AdminFormTag formTag, long recommendedCount) {
		this.formTag = formTag;
		this.recommendedCount = recommendedCount;
	}
}
