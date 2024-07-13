package org.layer.domain.retrospect.controller.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RetrospectCreateRequest(
	@Schema(description = "회고 제목", example = "중간 발표 이후 회고")
	@Size(min = 3)
	String title,
	@Schema(description = "회고 한줄 설명", example = "우리만의 KPT 회고")
	@NotNull
	String introduction,
	@Schema(description = "회고 질문 목록(리스트)", example = "이번에 가장 어려웠던 점은 무엇인가요?")
	@NotNull
	@Size(min = 3, max = 15)
	List<String> questions,
	@Schema(description = "내 회고 목록 추가 여부", example = "true")

	boolean isMyForm
) {
}
