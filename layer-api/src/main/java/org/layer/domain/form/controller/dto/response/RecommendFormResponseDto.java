package org.layer.domain.form.controller.dto.response;

import java.util.List;

import org.layer.domain.form.entity.Form;
import org.layer.domain.tag.entity.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RecommendFormResponseDto", description = "회고 템플릿 응답 Dto")
public record RecommendFormResponseDto(
	@Schema(description = "추천 템플릿 id \n 1. 추천템플릿 설정 API에 해당 데이터 사용 \n 2. 자세한 내용 확인하는 API 사용할 때 해당 데이터 사용", example = "3")
	Long formId,
	@Schema(description = "템플릿(폼) 이름", example = "차근차근, 단계적인 회고")
	String formName,
	@Schema(description = "태그", example = "[\"KPT\", \"태그2\"]")
	List<String> tags,
	@Schema(description = "템플릿(폼) 이미지 url", example = "[url 형식]")
	String formImageUrl

) {
	public static RecommendFormResponseDto of(Form form, List<Tag> tags){

		List<String> tagNames = tags.stream()
			.map(Tag::getTagName)
			.toList();

		return new RecommendFormResponseDto(form.getId(), form.getTitle(), tagNames, null);
	}
}
