package org.layer.domain.space;

import static org.assertj.core.api.Assertions.*;
import static org.layer.global.exception.SpaceExceptionType.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.layer.domain.space.controller.dto.SpaceRequest;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.exception.SpaceException;
import org.layer.domain.space.repository.SpaceRepository;
import org.layer.domain.space.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class SpaceServiceTest {

	@Autowired
	private SpaceService spaceService;

	@Autowired
	private SpaceRepository spaceRepository;

	@Nested
	class 스페이스_수정{

		@Test
		@DisplayName("스페이스를 수정할 수 있다.")
		void updateSpaceTest1(){
			// given
			Long memberId = 1L;
			Space space = SpaceFixture.createSpaceFixture(memberId);
			Space savedSpace = spaceRepository.save(space);

			SpaceRequest.UpdateSpaceRequest request = new SpaceRequest.UpdateSpaceRequest("new url",
				savedSpace.getId(), "새 스페이스 이름1", "새 스페이스 소개1");

			// when
			spaceService.updateSpace(memberId, request);

			// then
			Optional<Space> foundSpace = spaceRepository.findById(savedSpace.getId());
			assertThat(foundSpace).isNotEmpty();
			assertThat(foundSpace.get().getBannerUrl()).isEqualTo("new url");
			assertThat(foundSpace.get().getName()).isEqualTo("새 스페이스 이름1");
			assertThat(foundSpace.get().getIntroduction()).isEqualTo("새 스페이스 소개1");
		}

		@Test
		@DisplayName("리더가 아닐 경우, 스페이스를 수정할 수 없다.")
		void updateSpaceTest2(){
			// given
			Long memberId = 2L;
			Space space = SpaceFixture.createSpaceFixture(1L);
			Space savedSpace = spaceRepository.save(space);

			SpaceRequest.UpdateSpaceRequest request = new SpaceRequest.UpdateSpaceRequest("new url",
				savedSpace.getId(), "새 스페이스 이름1", "새 스페이스 소개1");

			// when, then
			assertThatThrownBy(() -> spaceService.updateSpace(memberId, request))
				.isInstanceOf(SpaceException.class)
				.hasMessageContaining(CAN_ONLY_SPACE_LEADER.message());
		}
	}
}
