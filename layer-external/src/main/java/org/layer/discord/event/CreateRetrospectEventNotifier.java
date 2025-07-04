package org.layer.discord.event;

import org.layer.discord.DiscordAppender;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class CreateRetrospectEventNotifier {

	private final DiscordAppender discordAppender;

	@EventListener
	public void handleSignUpEvent(CreateRetrospectEvent event) {
		discordAppender.createRetrospectAppend(
			event.title(),
			event.memberId(),
			event.createdDate());
	}
}
