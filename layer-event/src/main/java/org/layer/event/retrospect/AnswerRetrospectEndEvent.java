package org.layer.event.retrospect;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record AnswerRetrospectEndEvent(
	String eventId,
	LocalDateTime eventTime,
	Long memberId,
	Long spaceId,
	Long retrospectId,
	String answerContent
) implements BaseEvent {
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
