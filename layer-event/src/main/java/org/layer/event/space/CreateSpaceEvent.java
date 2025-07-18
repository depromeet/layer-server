package org.layer.event.space;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record CreateSpaceEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime,
	String title,
	String category
) implements BaseEvent {
	public static CreateSpaceEvent of(String eventId, Long memberId, LocalDateTime eventTime, String title, String category) {
		return new CreateSpaceEvent(eventId, memberId, eventTime, title, category);
	}
}
