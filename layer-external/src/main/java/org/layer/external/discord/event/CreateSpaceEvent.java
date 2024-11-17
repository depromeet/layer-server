package org.layer.external.discord.event;

import java.time.LocalDateTime;

public record CreateSpaceEvent(
	String title,
	Long memberId,
	LocalDateTime createdDate
) {
	public static CreateSpaceEvent of(String title, Long memberId, LocalDateTime now){
		return new CreateSpaceEvent(title, memberId, now);
	}
}
