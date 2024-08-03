package layer.domain.retrospect.repository;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RetrospectRepositoryTest {
	@Autowired
	private RetrospectRepository retrospectRepository;

	@Test
	void 멀티모듈_레포지토리_테스트() {
		// given
		Retrospect retrospect = new Retrospect(1L, "회고제목입니다", "회고소개입니다", RetrospectStatus.PROCEEDING,
			LocalDateTime.of(2024, 8, 4, 3, 5));

		// when
		Retrospect saved = retrospectRepository.save(retrospect);

		// then
		Assertions.assertThat(saved)
			.extracting("id", "spaceId", "title", "introduction", "retrospectStatus")
			.containsExactly(1L, 1L, "회고제목입니다", "회고소개입니다", RetrospectStatus.PROCEEDING);
	}
}
