package org.layer.domain.retrospect.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.form.controller.dto.response.CustomTemplateResponse;
import org.layer.domain.form.enums.FormTag;

import java.time.LocalDateTime;

@Schema(name = "QuestionCreateRequest", description = "질문 객체 생성 요청 Dto")
public record QuestionCreateRequest(
	@Schema(description = "질문 내용", example = "팀원 간의 소통은 어땠나요?")
	String questionContent,
	@Schema(description = "질문 타입", example = "plain_text")
	String questionType
) {

	public static CustomTemplateResponse of(String title, FormTag formTag, LocalDateTime createdAt) {
		return CustomTemplateResponse.builder()
				.title(title)
				.formTag(formTag.getTag())
				.createdAt(createdAt)
				.build();
	}
}
