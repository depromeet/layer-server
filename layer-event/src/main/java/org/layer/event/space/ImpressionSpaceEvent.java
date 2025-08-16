package org.layer.event.space;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record ImpressionSpaceEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime
) implements BaseEvent {
}
