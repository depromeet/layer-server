package org.layer.admin.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MemberActivityCache {
	public final Map<Long, Map<String, Integer>> activityMap = new ConcurrentHashMap<>();

	public String getMessage(){
		if (activityMap.isEmpty()) {
			return "📊 현재 집계된 활동 데이터가 없습니다.";
		}

		StringBuilder message = new StringBuilder();
		message.append("\n---\n\n");
		message.append("📊 [일일 유저 API 호출 통계]\n\n");

		activityMap.forEach((memberId, activity) -> {
			message.append("👤 유저 ID: ").append(memberId).append("\n");

			// 호출 횟수 기준 내림차순 정렬 후 최대 5개까지만 출력
			activity.entrySet().stream()
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.limit(5)
				.forEach(entry ->
					message.append("  - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("회\n")
				);

			message.append("\n---\n\n");
		});

		return message.toString();
	}

}
