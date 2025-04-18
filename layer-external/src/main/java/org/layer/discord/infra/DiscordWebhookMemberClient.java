package org.layer.discord.infra;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "DiscordWebhookMemberClient", url = "${discord.webhook.member-url}")
public interface DiscordWebhookMemberClient {
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	void sendNotification(@RequestBody Map<String, Object> body);
}
