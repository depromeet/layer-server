package org.layer.domain.space;

import static org.assertj.core.api.Assertions.*;
import static org.layer.global.exception.ApiSpaceExceptionType.*;
import static org.layer.global.exception.SpaceExceptionType.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.layer.domain.fixture.FormFixture;
import org.layer.domain.fixture.MemberFixture;
import org.layer.domain.fixture.MemberSpaceRelationFixture;
import org.layer.domain.fixture.SpaceFixture;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.enums.FormTag;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.domain.space.controller.dto.SpaceRequest;
import org.layer.domain.space.controller.dto.SpaceResponse;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.exception.SpaceException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.layer.domain.space.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SpaceServiceTest {

	@Autowired
	private SpaceService spaceService;

	@Autowired
	private SpaceRepository spaceRepository;

	@Autowired
	private FormRepository formRepository;

	@Autowired
	private MemberSpaceRelationRepository memberSpaceRelationRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Nested
	class 스페이스_수정 {

		@Test
		@DisplayName("스페이스를 수정할 수 있다.")
		void updateSpaceTest1() {
			// given
			Long memberId = 1L;
			Space space = SpaceFixture.createFixture(memberId, 10001L);
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
		void updateSpaceTest2() {
			// given
			Long memberId = 2L;
			Space space = SpaceFixture.createFixture(1L, 10001L);
			Space savedSpace = spaceRepository.save(space);

			SpaceRequest.UpdateSpaceRequest request = new SpaceRequest.UpdateSpaceRequest("new url",
				savedSpace.getId(), "새 스페이스 이름1", "새 스페이스 소개1");

			// when, then
			assertThatThrownBy(() -> spaceService.updateSpace(memberId, request))
				.isInstanceOf(SpaceException.class)
				.hasMessageContaining(CAN_ONLY_SPACE_LEADER.message());
		}
	}

	@Nested
	class 스페이스_단건조회 {
		@Test
		@DisplayName("스페이스 단건 조회를 할 수 있다.")
		void getSpaceByIdTest1() {
			// given
			Form form = FormFixture.createFixture(null, null);
			Form savedForm = formRepository.save(form);

			Member member = MemberFixture.createFixture();

			Member savedMember = memberRepository.save(member);
			Long memberId = savedMember.getId();
			Long anotherMemberId = 10003L;

			Space space = SpaceFixture.createFixture(memberId, savedForm.getId());
			Space savedSpace = spaceRepository.save(space);

			MemberSpaceRelation team = MemberSpaceRelationFixture.createFixture(savedSpace, memberId);
			MemberSpaceRelation team2 = MemberSpaceRelationFixture.createFixture(savedSpace, anotherMemberId);
			memberSpaceRelationRepository.saveAll(List.of(team, team2));

			// when
			SpaceResponse.SpaceWithMemberCountInfo res = spaceService.getSpaceById(memberId, savedSpace.getId());

			// then
			assertThat(res).isNotNull();
			assertThat(res.memberCount()).isEqualTo(2);
			assertThat(res.category()).isEqualTo(SpaceCategory.TEAM);
			assertThat(res.name()).isEqualTo("스페이스 이름1");
			assertThat(res.formTag()).isEqualTo(FormTag.KPT.getTag());
			assertThat(res.bannerUrl()).isEqualTo("url1");
			assertThat(res.leader().name()).isEqualTo("이름1");
		}

		@Test
		@DisplayName("속하지 않은 스페이스의 단건 조회는 불가능하다.")
		void getSpaceByIdTest2() {
			// given
			Form form = FormFixture.createFixture(null, null);
			Form savedForm = formRepository.save(form);

			Member member = MemberFixture.createFixture();

			Member savedMember = memberRepository.save(member);
			Long memberId = savedMember.getId();

			Space space = SpaceFixture.createFixture(memberId, savedForm.getId());
			Space savedSpace = spaceRepository.save(space);

			// when, then
			assertThatThrownBy(() -> spaceService.getSpaceById(memberId, savedSpace.getId()))
				.isInstanceOf(SpaceException.class)
				.hasMessageContaining(NOT_JOINED_SPACE.message());
		}
	}

	@Nested
	class 스페이스_생성 {
		@Test
		@DisplayName("스페이스를 생성할 수 있다.")
		void createSpaceTest1() {
			// given

			// when

			// then
		}
	}
}
