package org.layer.event.retrospect;

import java.time.LocalDateTime;

public record AnswerRetrospectStartEvent(
	String eventId,
	LocalDateTime eventTime,
	Long memberId,
	Long spaceId,
	Long retrospectId
) {
	public static AnswerRetrospectStartEvent of(
		String eventId,
		LocalDateTime eventTime,
		Long memberId,
		Long spaceId,
		Long retrospectId
	) {
		return new AnswerRetrospectStartEvent(
			eventId, eventTime, memberId, spaceId, retrospectId
		);
	}
}
