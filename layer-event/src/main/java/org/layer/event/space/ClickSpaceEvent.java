package org.layer.event.space;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record ClickSpaceEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime,
	Long spaceId,
	String retrospectStatus
) implements BaseEvent {
}
