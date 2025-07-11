package org.layer.event.retrospect;

import java.time.LocalDateTime;

public record WriteRetrospectStartEvent(
	String eventId,
	LocalDateTime eventTime,
	Long memberId,
	Long spaceId,
	Long retrospectId
) {
	public static WriteRetrospectStartEvent of(
		String eventId,
		LocalDateTime eventTime,
		Long memberId,
		Long spaceId,
		Long retrospectId
	) {
		return new WriteRetrospectStartEvent(
			eventId, eventTime, memberId, spaceId, retrospectId
		);
	}
}
