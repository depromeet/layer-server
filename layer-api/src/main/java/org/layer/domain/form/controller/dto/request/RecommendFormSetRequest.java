package org.layer.domain.form.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "RecommendFormSetRequest", description = "추천 템플릿 설정 요청 Dto")
public record RecommendFormSetRequest(
	@NotNull
	@Schema(description = "추천 템플릿 폼 id", example = "3")
	Long formId,
	@NotNull
	@Schema(description = "스페이스 id", example = "1")
	Long spaceId
) {
}
