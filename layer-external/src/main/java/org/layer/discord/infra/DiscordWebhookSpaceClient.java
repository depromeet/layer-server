package org.layer.discord.infra;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "DiscordWebhookSpaceClient", url = "${discord.webhook.space-url}")
public interface DiscordWebhookSpaceClient {
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	void sendNotification(@RequestBody Map<String, Object> body);
}
