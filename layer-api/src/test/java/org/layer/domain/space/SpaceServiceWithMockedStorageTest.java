package org.layer.domain.space;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.layer.domain.space.controller.dto.SpaceRequest;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.repository.SpaceRepository;
import org.layer.domain.space.service.SpaceService;
import org.layer.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SpaceServiceWithMockedStorageTest {

	@Autowired
	private SpaceService spaceService;

	@Autowired
	private SpaceRepository spaceRepository;

	@MockBean
	private StorageService mockStorageService;

	@Nested
	class 스페이스_생성 {
		@Test
		@DisplayName("스페이스를 생성할 수 있다.")
		void createSpaceTest1() {
			// given
			Long memberId = 1L;
			SpaceRequest.CreateSpaceRequest req = new SpaceRequest.CreateSpaceRequest("new url1",
				SpaceCategory.TEAM,"스페이스 이름1", "스페이스 소개1");

			doReturn(true).when(mockStorageService).validateBannerUrl(any());

			// when
			Long spaceId = spaceService.createSpace(memberId, req);

			// then
			Space space = spaceRepository.findByIdOrThrow(spaceId);
			assertThat(space.getName()).isEqualTo("스페이스 이름1");
			assertThat(space.getIntroduction()).isEqualTo("스페이스 소개1");
			assertThat(space.getBannerUrl()).isEqualTo("new url1");
			assertThat(space.getCategory()).isEqualTo(SpaceCategory.TEAM);
		}
	}
}
