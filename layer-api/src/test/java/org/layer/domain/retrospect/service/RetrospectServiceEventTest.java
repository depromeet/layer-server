package org.layer.domain.retrospect.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.layer.domain.common.time.Time;
import org.layer.domain.fixture.RetrospectFixture;
import org.layer.domain.fixture.SpaceFixture;
import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RetrospectServiceEventTest {

	@Autowired
	private RetrospectService retrospectService;
	@Autowired
	private SpaceRepository spaceRepository;
	@Autowired
	private RetrospectRepository retrospectRepository;
	@Autowired
	private TestAIAnalyzeEventListener eventListener;

	@Autowired
	private Time time;

	@Test
	void closeRetrospect_shouldEmitAIAnalyzeStartEvent() throws Exception {
		// given
		eventListener.reset();

		Long leaderId = 1L;
		Space space = SpaceFixture.createFixture(leaderId ,1L);
		space = spaceRepository.save(space);

		Retrospect retrospect = RetrospectFixture.createFixture(space.getId(), RetrospectStatus.PROCEEDING,
			AnalysisStatus.NOT_STARTED,
			time.now());
		retrospect = retrospectRepository.save(retrospect);

		// when
		retrospectService.closeRetrospect(space.getId(), retrospect.getId(), leaderId);

		// then
		assertThat(eventListener.await()).isTrue();
		assertThat(eventListener.getReceivedRetrospectId()).isEqualTo(retrospect.getId());
	}
}

