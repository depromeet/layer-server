package org.layer.event.retrospect;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record ClickRetrospectEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime,
	Long spaceId,
	Long retrospectId,
	String retrospectStatus

) implements BaseEvent {
}