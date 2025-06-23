package org.layer.discord.event;

import org.layer.discord.DiscordAppender;
import org.layer.event.member.SignUpEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class SignUpEventNotifier {
	private final DiscordAppender discordAppender;

	@EventListener
	public void handleSignUpEvent(SignUpEvent event) {
		discordAppender.createMember(
			event.name(),
			event.memberId(),
			event.eventTime());
	}
}
