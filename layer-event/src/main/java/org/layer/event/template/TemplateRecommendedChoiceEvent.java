package org.layer.event.template;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record TemplateRecommendedChoiceEvent(
	String eventId,
	Long memberId,
	String formTag,
	LocalDateTime eventTime
) implements BaseEvent {
}
