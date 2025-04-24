package org.layer.scheduler;

import java.time.LocalDateTime;

import org.layer.admin.cache.MemberActivityCache;
import org.layer.discord.event.MemberActivityEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MemberActivityScheduler {

	private final MemberActivityCache memberActivityCache;
	private final ApplicationEventPublisher eventPublisher;

	private final RedisTemplate<String, String> redisTemplate;

	// 매일 오후 9시에 실행됨
	@Scheduled(cron = "0 0 21 * * *")
	public void logDailyMemberActivity() {
		log.info("🌙 유저 활동 통계 작업 시작");

		eventPublisher.publishEvent(new MemberActivityEvent(memberActivityCache.activityMap));
		redisTemplate.opsForValue().set(LocalDateTime.now().toString(), memberActivityCache.getMessage());
		memberActivityCache.activityMap.clear();
	}
}
