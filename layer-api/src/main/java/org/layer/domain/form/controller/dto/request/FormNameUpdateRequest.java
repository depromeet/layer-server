package org.layer.domain.form.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "FormNameUpdateRequest", description = "커스템 템플릿 제목 변경 요청 queryParam")
public record FormNameUpdateRequest(
	@NotNull
	@Schema(description = "수정된 회고 제목", example = "수정된 회고 제목입니다.")
	String formTitle
) {
}
