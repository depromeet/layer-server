package org.layer.domain.form.controller.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DefaultFormGetResponse", description = "기본 회고폼(커스텀 템플릿) 조회 응답 Dto")
public record DefaultFormGetResponse(
	@Schema(description = "회고폼 이름", example = "KPT 커스텀 회고 폼")
	String title,
	@Schema(description = "질문 객체 목록", example = "")
	List<DefaultQuestionGetResponse> questions

) {
	public static DefaultFormGetResponse of(String title, List<DefaultQuestionGetResponse> questions){
		return new DefaultFormGetResponse(title, questions);
	}
}
