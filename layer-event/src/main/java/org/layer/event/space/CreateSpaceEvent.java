package org.layer.event.space;

import java.time.LocalDateTime;

public record CreateSpaceEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime,
	String title,
	String category

) {
	public static CreateSpaceEvent of(String eventId, Long memberId, LocalDateTime eventTime, String title, String category) {
		return new CreateSpaceEvent(eventId, memberId, eventTime, title, category);
	}
}
