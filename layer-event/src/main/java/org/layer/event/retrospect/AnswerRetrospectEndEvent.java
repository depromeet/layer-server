package org.layer.event.retrospect;

import java.time.LocalDateTime;

public record AnswerRetrospectEndEvent(
	String eventId,
	LocalDateTime eventTime,
	Long memberId,
	Long spaceId,
	Long retrospectId,
	String answerContent
) {
	public static AnswerRetrospectEndEvent of(
		String eventId,
		LocalDateTime eventTime,
		Long memberId,
		Long spaceId,
		Long retrospectId,
		String answerContent
	) {
		return new AnswerRetrospectEndEvent(
			eventId, eventTime, memberId, spaceId, retrospectId, answerContent
		);
	}
}
