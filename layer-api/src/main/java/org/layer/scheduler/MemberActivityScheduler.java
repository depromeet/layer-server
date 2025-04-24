package org.layer.scheduler;

import org.layer.admin.cache.MemberActivityCache;
import org.layer.discord.event.MemberActivityEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MemberActivityScheduler {

	private final MemberActivityCache memberActivityCache;
	private final ApplicationEventPublisher eventPublisher;

	// 매일 오후 9시에 실행됨
	@Scheduled(cron = "0 */3 * * * *", zone = "Asia/Seoul")
	public void logDailyMemberActivity() {
		log.info("🌙 유저 활동 통계 작업 시작");

		eventPublisher.publishEvent(new MemberActivityEvent(memberActivityCache.activityMap));
		memberActivityCache.activityMap.clear();
	}
}
