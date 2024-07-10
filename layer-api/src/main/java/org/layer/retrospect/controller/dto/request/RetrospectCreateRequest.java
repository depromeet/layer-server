package org.layer.retrospect.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "RetrospectCreateRequest", description = "회고 생성 요청 Dto")
public record RetrospectCreateRequest(
	@Schema(description = "회고 폼 id", example = "1")
	@NotNull
	Long formId,
	@Schema(description = "회고 제목", example = "중간 발표 이후 회고")
	@Size(min = 3)
	String title,
	@Schema(description = "회고 한줄 설명", example = "우리만의 KPT 회고")
	@NotNull
	String introduction

) {
}
