package org.layer.event.space;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record JoinSpaceEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime,
	Long spaceId
) implements BaseEvent {
	public static JoinSpaceEvent of(String eventId, Long memberId, LocalDateTime eventTime, Long spaceId) {
		return new JoinSpaceEvent(eventId, memberId, eventTime, spaceId);
	}
}
