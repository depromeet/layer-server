package org.layer.discord.notifier;

import org.layer.discord.DiscordAppender;
import org.layer.event.retrospect.CreateRetrospectEvent;
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
			event.eventTime());
	}
}
