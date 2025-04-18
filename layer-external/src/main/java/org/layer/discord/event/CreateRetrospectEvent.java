package org.layer.discord.event;

import java.time.LocalDateTime;

public record CreateRetrospectEvent(
	String title,
	Long memberId,
	LocalDateTime createdDate
) {
	public static CreateRetrospectEvent of(String title, Long memberId, LocalDateTime createdDate) {
		return new CreateRetrospectEvent(title, memberId, createdDate);
	}
}
