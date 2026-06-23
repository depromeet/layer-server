package org.layer.batch.retrospect;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.domain.answer.enums.AnswerStatus;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.common.time.Time;
import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.event.ai.AIAnalyzeStartEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetrospectScheduler {

	private final RetrospectRepository retrospectRepository;
	private final AnswerRepository answerRepository;
	private final ApplicationEventPublisher eventPublisher;

	private final Time time;

	/**
	 * @note: 1시간마다 실행된다.
	 */
	@Scheduled(cron = "0 0 * * * *")
	@Transactional
	public void updateRetrospectStatusToDone() {
		log.info("Batch Module: Batch Start : updateRetrospectStatusToDone");

		LocalDateTime now = time.now();

		List<Retrospect> retrospects = retrospectRepository.findAllByDeadlineBeforeAndRetrospectStatus(
			now, RetrospectStatus.PROCEEDING);

		updateRetrospectAndPublishEvent(retrospects);

		log.info("Batch Module: Batch End : updateRetrospectStatusToDone");
	}

	@Transactional
	public void updateRetrospectAndPublishEvent(List<Retrospect> retrospects) {
		retrospects.forEach(retrospect -> retrospect.completeRetrospectAndStartAnalysis(time.now()));
		retrospectRepository.saveAll(retrospects);

		retrospects.forEach(retrospect -> {
			boolean hasAnswers = answerRepository.existsByRetrospectIdAndAnswerStatus(
				retrospect.getId(), AnswerStatus.DONE);

			if (!hasAnswers) {
				log.info("No answers for retrospectId: {}. Skipping AI analysis.", retrospect.getId());
				retrospect.updateAnalysisStatus(AnalysisStatus.DONE);
				retrospectRepository.save(retrospect);
				return;
			}

			eventPublisher.publishEvent(AIAnalyzeStartEvent.of(retrospect.getId()));
		});
	}
}
