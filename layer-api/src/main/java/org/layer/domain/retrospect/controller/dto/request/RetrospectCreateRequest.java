package org.layer.domain.retrospect.controller.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RetrospectCreateRequest(
	@Schema(description = "회고 제목", example = "중간 발표 이후 회고")
	@NotNull
	String title,
	@Schema(description = "회고 한줄 설명", example = "우리만의 KPT 회고")
	String introduction,
	@Schema(description = "회고 질문 객체 목록", example = "")
	@NotNull
	List<QuestionCreateRequest> questions,
	@Schema(description = "회고 마감 일자", example = "")
	@NotNull
	LocalDateTime deadline,
	@Schema(description = "질문을 수정한 경우 true", example = "true")
	boolean isNewForm,
	@Schema(description = "질문을 수정한 경우, 변경된 폼 이름", example = "변경된 커스텀 폼 제목")
	String formName,
	@Schema(description = "질문을 수정한 경우, 변경된 폼 한줄 소개", example = "변경된 커스텀 폼 한줄 소개")
	String formIntroduction,
	@Schema(description = "질문을 수정한 경우, 현재 form id", example = "3")
	Long curFormId
) {
}
