package org.layer.event.retrospect;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record CreateRetrospectEvent(
	String eventId,
	Long spaceId,
	Long retrospectId,
	long targetAnswerCount,
	String title,
	Long memberId,
	LocalDateTime eventTime
) implements BaseEvent {
	public static CreateRetrospectEvent of(
		String eventId,
		Long spaceId,
		Long retrospectId,
		long targetAnswerCount,
		String title,
		Long memberId,
		LocalDateTime eventTime
	) {
		return new CreateRetrospectEvent(
			eventId, spaceId, retrospectId, targetAnswerCount, title, memberId, eventTime
		);
	}

}
