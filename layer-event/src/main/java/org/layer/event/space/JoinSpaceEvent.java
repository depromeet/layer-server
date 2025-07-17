package org.layer.event.space;

import java.time.LocalDateTime;

public record JoinSpaceEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime,
	Long spaceId
) {
	public static JoinSpaceEvent of(String eventId, Long memberId, LocalDateTime eventTime, Long spaceId) {
		return new JoinSpaceEvent(eventId, memberId, eventTime, spaceId);
	}
}
