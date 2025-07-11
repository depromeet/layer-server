package org.layer.event.retrospect;

import java.time.LocalDateTime;

public record WriteRetrospectEndEvent(
	String eventId,
	LocalDateTime eventTime,
	Long memberId,
	Long spaceId,
	Long retrospectId,
	String answerContent
) {
	public static WriteRetrospectEndEvent of(
		String eventId,
		LocalDateTime eventTime,
		Long memberId,
		Long spaceId,
		Long retrospectId,
		String answerContent
	) {
		return new WriteRetrospectEndEvent(
			eventId, eventTime, memberId, spaceId, retrospectId, answerContent
		);
	}
}
