package org.layer.domain.stats;

import org.layer.annotation.MemberId;
import org.layer.domain.common.random.CustomRandom;
import org.layer.domain.common.time.Time;
import org.layer.domain.form.enums.FormTag;
import org.layer.event.template.TemplateListViewClickEvent;
import org.layer.event.template.TemplateRecommendedChoiceEvent;
import org.layer.event.template.TemplateRecommendedClickEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StatsTemplateController implements StatsTemplateApi {
	private final ApplicationEventPublisher eventPublisher;
	private final CustomRandom random;
	private final Time time;

	@PostMapping("/stats/template/click/list-view")
	@Override
	public ResponseEntity<Void> publishTemplateListViewClickEvent(@MemberId Long memberId) {
		eventPublisher.publishEvent(
			new TemplateListViewClickEvent(random.generateRandomValue(), memberId, time.now()));
		return ResponseEntity.ok().body(null);
	}

	@PostMapping("/stats/template/choice/list-view")
	@Override
	public ResponseEntity<Void> publishTemplateListViewChoiceEvent(@MemberId Long memberId,
		@RequestParam FormTag formTag) {
		eventPublisher.publishEvent(
			new TemplateRecommendedChoiceEvent(random.generateRandomValue(), memberId, formTag.getTag(), time.now()));
		return ResponseEntity.ok().body(null);
	}

	@PostMapping("/stats/template/click/recommended")
	@Override
	public ResponseEntity<Void> publishTemplateRecommendedClickEvent(@MemberId Long memberId) {
		eventPublisher.publishEvent(
			new TemplateRecommendedClickEvent(random.generateRandomValue(), memberId, time.now()));
		return ResponseEntity.ok().body(null);
	}
}
