package org.layer.domain.form.controller.dto.request;

import org.layer.domain.form.enums.RetrospectPeriod;
import org.layer.domain.form.enums.RetrospectPurpose;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "RecommendFormQueryDto", description = "회고 템플릿 요청 queryParam")
public record RecommendFormQueryDto(
	@NotNull
	@Schema(description = "회고 주기적 여부", example = "true")
	boolean periodic,
	@Schema(description = "회고가 주기적일 때, 구체적인 주기", example = "WEEKLY")
	RetrospectPeriod period,
	@NotNull
	@Schema(description = "회고 목적", example = "TEAM_GROWTH")
	RetrospectPurpose purpose
) {
}
