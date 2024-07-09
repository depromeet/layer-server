package org.layer.retrospect.controller.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "RetrospectCreateRequest", description = "회고 생성 요청 Dto")
public record RetrospectCreateRequest(

	@Size(min = 1, max = 15)
	@Schema(description = "회고 질문", example = "어려움을 느껴서 개선하고 싶은 점은 무엇인가요?")
	List<String> questions,
	@Schema(description = "나의 회고 폼 추가 여부, true: 내 회고폼에 추가 O, false: 내 회고폼에 추가 X", example = "true")
	boolean isMyForm
) {
}
