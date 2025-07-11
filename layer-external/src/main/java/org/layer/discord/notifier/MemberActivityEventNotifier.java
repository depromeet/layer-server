package org.layer.discord.notifier;

import org.layer.discord.DiscordAppender;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class MemberActivityEventNotifier {
	private final DiscordAppender discordAppender;

	@EventListener
	public void handleSignUpEvent(MemberActivityEvent event) {
		discordAppender.aggregateMemberActivity(event.activities());
	}
}
