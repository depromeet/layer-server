package org.layer.discord.notifier;

import java.time.LocalDateTime;

public record ErrorEvent(
	String message,
	String stackTrace,
	LocalDateTime now
) {
	public static ErrorEvent of(String message, String stackTrace, LocalDateTime now){
		return new ErrorEvent(message, stackTrace, now);
	}
}
