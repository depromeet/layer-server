package org.layer.batch.retrospect;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.listener.TestAIAnalyzeEventListener;
import org.layer.domain.common.time.Time;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RetrospectSchedulerTest {

	@Autowired
	private RetrospectRepository retrospectRepository;
	@Autowired
	private RetrospectScheduler retrospectScheduler;
	@Autowired
	private TestAIAnalyzeEventListener eventListener;

	@Autowired
	private Time time;

	@BeforeEach
	void setUp() {
		eventListener.reset();
	}

	@Test
	void updateRetrospectStatusToDone_shouldUpdateStatusAndPublishEvent() throws Exception {
		// given
		Retrospect retrospect = RetrospectFixture.createFixture(1L, RetrospectStatus.PROCEEDING,
			AnalysisStatus.NOT_STARTED, time.now().minusHours(1));
		retrospect = retrospectRepository.save(retrospect);

		// when
		retrospectScheduler.updateRetrospectStatusToDone();

		// then
		Retrospect updated = retrospectRepository.findById(retrospect.getId()).orElseThrow();

		assertThat(updated.getRetrospectStatus()).isEqualTo(RetrospectStatus.DONE);
		assertThat(updated.getAnalysisStatus()).isEqualTo(AnalysisStatus.PROCEEDING);

		// 이벤트 수신 확인
		assertThat(eventListener.await()).isTrue();
		assertThat(eventListener.getReceivedRetrospectId()).isEqualTo(retrospect.getId());
	}
}
