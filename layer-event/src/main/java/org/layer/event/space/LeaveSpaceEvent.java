package org.layer.event.space;

import java.time.LocalDateTime;

public record LeaveSpaceEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime,
	Long spaceId
) {
	public static LeaveSpaceEvent of(String eventId, Long memberId, LocalDateTime eventTime, Long spaceId) {
		return new LeaveSpaceEvent(eventId, memberId, eventTime, spaceId);
	}
}
