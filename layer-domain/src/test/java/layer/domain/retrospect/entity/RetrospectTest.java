package layer.domain.retrospect.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;

public class RetrospectTest {

	@Test
	void 진행중인_회고는_진행여부로직에서_예외를_발생시키지_않는다(){
		// given
		Retrospect retrospect = new Retrospect(1L, "회고제목입니다", "회고소개입니다", RetrospectStatus.PROCEEDING);

		// when
		retrospect.isProceedingRetrospect();

		Assertions.assertThat(retrospect.getRetrospectStatus()).isEqualTo(RetrospectStatus.PROCEEDING);
	}
}
