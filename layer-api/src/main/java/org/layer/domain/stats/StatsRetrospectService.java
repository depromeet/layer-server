package org.layer.domain.stats;

import java.util.List;
import java.util.Optional;

import org.layer.domain.common.random.CustomRandom;
import org.layer.domain.common.time.Time;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.event.retrospect.ClickRetrospectEvent;
import org.layer.event.space.ClickSpaceEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsRetrospectService {
	private final RetrospectRepository retrospectRepository;

	private final ApplicationEventPublisher eventPublisher;
	private final CustomRandom random;
	private final Time time;

	public void clickSpace(Long spaceId, Long memberId) {

		List<Retrospect> retrospects = retrospectRepository.findAllBySpaceId(spaceId);
		if(retrospects.isEmpty()) {
			return;
		}

		Optional<Retrospect> proceedingRetrospect = retrospects.stream()
			.filter(Retrospect::isRetrospectProceeding)
			.findFirst();

		RetrospectStatus retrospectStatus = null;
		 if(proceedingRetrospect.isEmpty()) {
			retrospectStatus = RetrospectStatus.DONE;
		} else {
			retrospectStatus = RetrospectStatus.PROCEEDING;
		}

		eventPublisher.publishEvent(new ClickSpaceEvent(
			random.generateRandomValue(),
			memberId,
			time.now(),
			spaceId,
			retrospectStatus.name()
		));
	}

	public void clickRetrospect(Long retrospectId, Long memberId) {
		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);

		eventPublisher.publishEvent(new ClickRetrospectEvent(
			random.generateRandomValue(),
			memberId,
			time.now(),
			retrospect.getSpaceId(),
			retrospectId,
			retrospect.getRetrospectStatus().name()
		));
	}
}
