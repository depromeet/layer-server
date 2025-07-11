package org.layer.discord.notifier;

import org.layer.discord.DiscordAppender;
import org.layer.event.space.CreateSpaceEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class CreateSpaceEventNotifier {

	private final DiscordAppender discordAppender;

	@EventListener
	public void handleSignUpEvent(CreateSpaceEvent event) {
		discordAppender.createSpaceAppend(
			event.title(),
			event.memberId(),
			event.eventTime());
	}
}
