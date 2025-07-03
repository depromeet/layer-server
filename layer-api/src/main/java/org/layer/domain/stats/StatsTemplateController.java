package org.layer.domain.stats;

import org.layer.annotation.MemberId;
import org.layer.domain.common.random.CustomRandom;
import org.layer.domain.common.time.Time;
import org.layer.event.template.TemplateListViewEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StatsTemplateController {
	private final ApplicationEventPublisher eventPublisher;
	private final CustomRandom random;
	private final Time time;

	@PostMapping("/stats/template/list-view")
	public ResponseEntity<Void> publishTemplateListViewEvent(@MemberId Long memberId) {
		eventPublisher.publishEvent(
			new TemplateListViewEvent(random.generateRandomValue(), memberId, time.now()));
		return ResponseEntity.ok().body(null);
	}
}
