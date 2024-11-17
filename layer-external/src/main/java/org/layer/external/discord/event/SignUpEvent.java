package org.layer.external.discord.event;

import java.time.LocalDateTime;

public record SignUpEvent(
	String name,
	Long memberId,
	LocalDateTime createdDate
) {
	public static SignUpEvent of(String name, Long memberId, LocalDateTime now){
		return new SignUpEvent(name, memberId, now);
	}
}
