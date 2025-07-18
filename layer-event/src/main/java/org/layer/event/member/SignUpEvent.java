package org.layer.event.member;

import java.time.LocalDateTime;

import org.layer.event.BaseEvent;

public record SignUpEvent(
	String eventId,
	Long memberId,
	String name,
	LocalDateTime eventTime,
	String memberRole
) implements BaseEvent {
}
