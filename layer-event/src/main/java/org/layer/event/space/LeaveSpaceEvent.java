package org.layer.event.space;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record LeaveSpaceEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime,
	Long spaceId
) implements BaseEvent {
	public static LeaveSpaceEvent of(String eventId, Long memberId, LocalDateTime eventTime, Long spaceId) {
		return new LeaveSpaceEvent(eventId, memberId, eventTime, spaceId);
	}
}
