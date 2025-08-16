package org.layer.event.retrospect;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record ImpressionRetrospectEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime
) implements BaseEvent {
}
