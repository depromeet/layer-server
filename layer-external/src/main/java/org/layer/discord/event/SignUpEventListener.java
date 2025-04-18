package org.layer.discord.event;

import org.layer.discord.DiscordAppender;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class SignUpEventListener {
	private final DiscordAppender discordAppender;

	@EventListener
	public void handleSignUpEvent(SignUpEvent event) {
		discordAppender.createMember(
			event.name(),
			event.memberId(),
			event.createdDate());
	}
}
