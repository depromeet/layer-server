package org.layer.domain.retrospect.service;


import org.layer.domain.member.repository.MemberRepository;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RetrospectServiceTest {

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
//
//   @Test
//   void 정상입력의_경우_회고생성에_성공한다() {
//       // given
//       // TODO : 이 부분은 이렇게 처리할지 or data.sql로 처리할지 고민.
//       Member member = Member.builder()
//               .name("홍길동")
//               .email("qwer@naver.com")
//               .memberRole(MemberRole.USER)
//               .socialType(SocialType.GOOGLE)
//               .socialId("123456789")
//               .build();
//       Member member1 = memberRepository.saveAndFlush(member);
//
//       Space space = new Space(null, SpaceCategory.TEAM, List.of(DEVELOPMENT), "회고스페이스이름입니다", "회고스페이스 설명입니다", 1L, 1L);
//       spaceRepository.saveAndFlush(space);
//       memberSpaceRelationRepository.saveAndFlush(new MemberSpaceRelation(member1.getId(), space));
//
//       RetrospectCreateRequest request = new RetrospectCreateRequest("회고제목입니다", "회고소개입니다",
//               List.of(new QuestionCreateRequest("질문1", QuestionType.PLAIN_TEXT.getStyle()),
//                       new QuestionCreateRequest("질문2", QuestionType.PLAIN_TEXT.getStyle()),
//                       new QuestionCreateRequest("질문3", QuestionType.PLAIN_TEXT.getStyle())),
//               LocalDateTime.of(2024, 8, 4, 3, 5),
//               false, null, null, null);
//       // when
//       Long savedId = retrospectService.createRetrospect(request, 1L, 1L);
//
//       // then
//       Retrospect retrospect = retrospectRepository.findByIdOrThrow(savedId);
//       Assertions.assertThat(retrospect)
//               .extracting("title", "introduction", "retrospectStatus")
//               .containsExactly("회고제목입니다", "회고소개입니다", RetrospectStatus.PROCEEDING);
//   }
}
