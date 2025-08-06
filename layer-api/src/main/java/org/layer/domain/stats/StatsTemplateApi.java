package org.layer.domain.stats;

import org.layer.annotation.MemberId;
import org.layer.domain.form.enums.FormTag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "[어드민] 템플릿 관련", description = "템플릿 관련한 어드민 기능 API")
public interface StatsTemplateApi {

	@Operation(summary = "템플릿 리스트 보기 클릭 이벤트", description = "템플릿 리스트 보기를 클릭했을 때 해당 API를 호출합니다.")
	ResponseEntity<Void> publishTemplateListViewClickEvent(@MemberId Long memberId);

	@Operation(summary = "템플릿 리스트 보기 내에서 선택 이벤트", description = "템플릿 리스트에서 특정 템플릿을 선택했을 때 해당 API를 호출합니다.")
	ResponseEntity<Void> publishTemplateListViewChoiceEvent(@MemberId Long memberId, @RequestParam FormTag formTag);

	@Operation(summary = "템플릿 추천 클릭 이벤트", description = "템플릿 추천을 클릭했을 때 해당 API를 호출합니다.")
	ResponseEntity<Void> publishTemplateRecommendedClickEvent(@MemberId Long memberId);
}
