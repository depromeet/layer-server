package org.layer.event.member;

import java.time.LocalDateTime;

public record SignUpEvent(
	String eventId,
	Long memberId,
	String name,
	LocalDateTime eventTime
) {
}
