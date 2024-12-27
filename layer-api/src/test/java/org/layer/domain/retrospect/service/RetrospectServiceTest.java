package org.layer.domain.retrospect.service;

import static org.layer.domain.space.entity.SpaceField.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.retrospect.controller.dto.request.QuestionCreateRequest;
import org.layer.domain.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
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
			Member member = Member.builder()
				.name("홍길동")
				.email("qwer@naver.com")
				.memberRole(MemberRole.USER)
				.socialType(SocialType.GOOGLE)
				.socialId("123456789")
				.build();
			Member member1 = memberRepository.saveAndFlush(member);

			Space space = new Space("banner-url", SpaceCategory.TEAM, List.of(DEVELOPMENT), "회고스페이스 이름", "회고스페이스 설명",
				1L, 1L);
			spaceRepository.saveAndFlush(space);
			memberSpaceRelationRepository.saveAndFlush(new MemberSpaceRelation(member1.getId(), space));

			RetrospectCreateRequest request = new RetrospectCreateRequest("회고제목입니다", "회고소개입니다",
				List.of(new QuestionCreateRequest("질문1", QuestionType.PLAIN_TEXT.getStyle()),
					new QuestionCreateRequest("질문2", QuestionType.PLAIN_TEXT.getStyle()),
					new QuestionCreateRequest("질문3", QuestionType.PLAIN_TEXT.getStyle())),
				LocalDateTime.of(2024, 8, 4, 3, 5),
				false, null, null, null, false);

			// when
			Long savedId = retrospectService.createRetrospect(request, 1L, 1L);

			// then
			Retrospect retrospect = retrospectRepository.findByIdOrThrow(savedId);
			Assertions.assertThat(retrospect)
				.extracting("title", "introduction", "retrospectStatus")
				.containsExactly("회고제목입니다", "회고소개입니다", RetrospectStatus.PROCEEDING);
		}
	}

}
