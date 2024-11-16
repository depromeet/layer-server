package org.layer.external.discord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.layer.external.discord.infra.DiscordWebhookMemberClient;
import org.layer.external.discord.infra.DiscordWebhookRetrospectClient;
import org.layer.external.discord.infra.DiscordWebhookSpaceClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class DiscordAppender {

	private final DiscordWebhookRetrospectClient retrospectClient;
	private final DiscordWebhookMemberClient memberClient;
	private final DiscordWebhookSpaceClient spaceClient;

	public void createRetrospectAppend(String title, Long memberId, LocalDateTime now) {
		String content = "회고";
		int green = 3066993;
		Map<String, Object> body = getMessage(title, memberId, now, content, green);

		retrospectClient.sendNotification(body);
	}

	public void createSpaceAppend(String title, Long memberId, LocalDateTime now) {
		String content = "스페이스";
		int blue = 65280;
		Map<String, Object> body = getMessage(title, memberId, now, content, blue);

		spaceClient.sendNotification(body);
	}

	public void createMember(String name, Long memberId, LocalDateTime now){
		Map<String, Object> embed = new HashMap<>();
		embed.put("title", "\uD83D\uDE80[회원 가입] 새로운 유저가 가입하였습니다.\uD83D\uDE80");
		embed.put("description", memberId + " 번째 유저가 회원가입하였습니다.");
		embed.put("color", 15105570);

		// 필드 추가
		List<Map<String, String>> fields = List.of(
			Map.of("name", "유저 ID", "value", memberId.toString(), "inline", "true"),
			Map.of("name", "이름", "value", name, "inline", "true"),
			Map.of("name", "생성 시간", "value", now.toString(), "inline", "false")
		);
		embed.put("fields", fields);

		// 요청 Body 구성
		Map<String, Object> body = new HashMap<>();
		body.put("content", "회원 가입 알림");
		body.put("embeds", List.of(embed));

		memberClient.sendNotification(body);
	}

	private Map<String, Object> getMessage(String title, Long memberId, LocalDateTime now, String content, int color) {
		// Embeds 객체 구성
		Map<String, Object> embed = new HashMap<>();
		embed.put("title", title);
		embed.put("description", memberId + " 번 유저가 '" + title + "' " + content + "를 생성했습니다.");
		embed.put("color", color); // 컬러 코드 (디스코드 RGB 정수값)

		// 필드 추가
		List<Map<String, String>> fields = List.of(
			Map.of("name", "유저 ID", "value", memberId.toString(), "inline", "true"),
			Map.of("name", "제목", "value", title, "inline", "true"),
			Map.of("name", "생성 시간", "value", now.toString(), "inline", "false")
		);
		embed.put("fields", fields);

		// 요청 Body 구성
		Map<String, Object> body = new HashMap<>();
		body.put("content", "새 " + content + " 생성 알림");
		body.put("embeds", List.of(embed));
		return body;
	}

}
