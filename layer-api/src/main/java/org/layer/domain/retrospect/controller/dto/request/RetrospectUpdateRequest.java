package org.layer.domain.retrospect.controller.dto.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RetrospectUpdateRequest", description = "회고 수정 요청 Dto")
public record RetrospectUpdateRequest(
	@Schema(description = "회고 제목", example = "수정된 회고 제목입니다.")
	String title,
	@Schema(description = "회고 제목", example = "수정된 회고 제목입니다.")
	String introduction,
	@Schema(description = "회고 마감 기한", example = "2024-08-04T15:30:00")
	LocalDateTime deadline
) {
}
