package org.layer.domain.form.controller.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DefaultFormGetResponse", description = "기본 회고폼(커스텀 템플릿) 조회 응답 Dto")
public record FormGetResponse(
	@Schema(description = "회고폼 이름", example = "KPT 커스텀 회고 폼")
	String title,
	@Schema(description = "태그", example = "[\"KPT\", \"태그2\"]")
	String tag,
	@Schema(description = "질문 객체 목록", example = "")
	List<QuestionGetResponse> questions

) {
	public static FormGetResponse of(String title, String tag, List<QuestionGetResponse> questions){

		return new FormGetResponse(title, tag, questions);
	}
}
