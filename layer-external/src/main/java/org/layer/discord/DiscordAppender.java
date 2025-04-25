package org.layer.discord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.layer.discord.infra.DiscordWebhookErrorClient;
import org.layer.discord.infra.DiscordWebhookMemberActivityClient;
import org.layer.discord.infra.DiscordWebhookMemberClient;
import org.layer.discord.infra.DiscordWebhookRetrospectClient;
import org.layer.discord.infra.DiscordWebhookSpaceClient;
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
	private final DiscordWebhookErrorClient errorClient;

	private final DiscordWebhookMemberActivityClient memberActivityClient;

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

	public void createMember(String name, Long memberId, LocalDateTime now) {
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

	public void createErrorAppend(String message, String stackTrace) {
		Map<String, Object> body = new HashMap<>();
		body.put("title", "레이어 서버 에러 발생");

		Map<String, String> field1 = new HashMap<>();
		field1.put("name", "발생시각");
		field1.put("value", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));

		Map<String, String> field2 = new HashMap<>();
		field2.put("name", "에러 명");
		field2.put("value", message);

		Map<String, String> field3 = new HashMap<>();
		field3.put("name", "스택 트레이스");
		String substring = stackTrace.substring(0, 150);
		field3.put("value", substring);

		body.put("fields", List.of(field1, field2, field3));

		Map<String, Object> payload = new HashMap<>();
		payload.put("embeds", new Object[] {body});

		errorClient.sendNotification(payload);
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

	public void aggregateMemberActivity(Map<Long, Map<String, Integer>> activities) {
		if (activities.isEmpty()) return;

		StringBuilder message = new StringBuilder();

		LocalDate today = LocalDate.now();
		String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

		message.append("📊 " + formattedDate + " [일일 유저 API 호출 통계]\n\n");

		for (Map.Entry<Long, Map<String, Integer>> entry : activities.entrySet()) {
			Long memberId = entry.getKey();
			Map<String, Integer> summaryMap = entry.getValue();

			message.append("👤 유저 ID: ").append(memberId).append("\n");

			// count 높은 순으로 최대 5개 출력
			AtomicInteger idx = new AtomicInteger(1);

			summaryMap.entrySet().stream()
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.limit(5)
				.forEach(e ->
					message.append(idx.getAndIncrement() + ". ").append(e.getKey()).append(": ").append(e.getValue()).append("회\n")
				);

			message.append("---\n\n");
		}

		Map<String, Object> payload = Map.of("content", message.toString());
		memberActivityClient.sendNotification(payload);

		log.info("✅ Discord에 유저 활동 통계 전송 완료");
	}

}
