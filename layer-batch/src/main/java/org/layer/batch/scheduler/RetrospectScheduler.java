package org.layer.batch.scheduler;

import java.time.LocalDateTime;

import org.layer.domain.common.time.Time;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetrospectScheduler {

	private final RetrospectRepository retrospectRepository;
	private final Time time;

	/**
	 * @note: 1시간마다 실행된다.
	 * */
	@Scheduled(cron = "0 0 * * * *")
	public void updateRetrospectStatusToDone() {
		LocalDateTime now = time.now();
		retrospectRepository.updateRetrospectStatus(now);
		log.info("Batch : updateRetrospectStatusToDone");
	}

}