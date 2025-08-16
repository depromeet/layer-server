package org.layer.domain.retrospect.service;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.layer.domain.fixture.MemberFixture;
import org.layer.domain.fixture.RetrospectFixture;
import org.layer.domain.fixture.SpaceFixture;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.retrospect.controller.dto.request.QuestionCreateRequest;
import org.layer.domain.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RetrospectServiceTest {

	@Autowired
	private RetrospectService retrospectService;

	@Autowired
	private RetrospectRepository retrospectRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private SpaceRepository spaceRepository;

	@Autowired
	private MemberSpaceRelationRepository memberSpaceRelationRepository;

	@Nested
	class 회고_생성 {
		@Test
		@DisplayName("정상 입력의 경우 회고 생성에 성공한다.")
		void createRetrospectTest_1() {
			// given
			Member member = MemberFixture.createFixture("social-1");
			member = memberRepository.save(member);

			Space space = SpaceFixture.createFixture(member.getId(), 1L);
			spaceRepository.save(space);
			memberSpaceRelationRepository.save(new MemberSpaceRelation(member.getId(), space));

			RetrospectCreateRequest request = new RetrospectCreateRequest("회고제목입니다", "회고소개입니다",
				List.of(new QuestionCreateRequest("질문1", QuestionType.PLAIN_TEXT.getStyle()),
					new QuestionCreateRequest("질문2", QuestionType.PLAIN_TEXT.getStyle()),
					new QuestionCreateRequest("질문3", QuestionType.PLAIN_TEXT.getStyle())),
				LocalDateTime.of(2024, 8, 4, 3, 5),
				false, null, null, null, false);

			// when
			Long savedId = retrospectService.createRetrospect(request, space.getId(), member.getId());

			// then
			Retrospect retrospect = retrospectRepository.findByIdOrThrow(savedId);
			Assertions.assertThat(retrospect)
				.extracting("title", "introduction", "retrospectStatus")
				.containsExactly("회고제목입니다", "회고소개입니다", RetrospectStatus.PROCEEDING);
		}
	}

	@Nested
	class 회고_마감 {
		@Test
		@DisplayName("마감기한이 미지정된 회고를 마감할 경우, 마감기한이 현재시간으로 설정된다.")
		void closeRetrospectWithoutDeadline() {
			// given
			Member member = MemberFixture.createFixture("social-1");
			member = memberRepository.save(member);

			Space space = SpaceFixture.createFixture(member.getId(), 1L);
			spaceRepository.save(space);
			memberSpaceRelationRepository.save(new MemberSpaceRelation(member.getId(), space));

			Retrospect retrospect = RetrospectFixture.createFixture(space.getId(),
				RetrospectStatus.PROCEEDING, AnalysisStatus.NOT_STARTED, null);

			retrospect = retrospectRepository.save(retrospect);

			// when
			retrospectService.closeRetrospect(space.getId(), retrospect.getId(), member.getId());

			// then
			Retrospect savedRetrospect = retrospectRepository.findByIdOrThrow(retrospect.getId());

			Assertions.assertThat(savedRetrospect.getDeadline()).isNotNull();
			Assertions.assertThat(retrospect.getDeadline()).isBeforeOrEqualTo(LocalDateTime.now());
		}
	}

}
