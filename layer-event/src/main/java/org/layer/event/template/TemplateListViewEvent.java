package org.layer.event.template;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record TemplateListViewEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime
) implements BaseEvent {
}
