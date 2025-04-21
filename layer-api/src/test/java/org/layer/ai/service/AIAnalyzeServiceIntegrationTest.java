package org.layer.ai.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.layer.ai.OpenAIResponseFixture;
import org.layer.domain.analyze.entity.Analyze;
import org.layer.domain.analyze.enums.AnalyzeType;
import org.layer.domain.analyze.repository.AnalyzeRepository;
import org.layer.domain.answer.entity.Answer;
import org.layer.domain.answer.enums.AnswerStatus;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.common.time.Time;
import org.layer.domain.fixture.MemberSpaceRelationFixture;
import org.layer.domain.fixture.QuestionFixture;
import org.layer.domain.fixture.RetrospectFixture;
import org.layer.domain.fixture.SpaceFixture;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@EnableAsync
@Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AIAnalyzeServiceIntegrationTest {

	@Autowired
	private SpaceRepository spaceRepository;
	@Autowired
	private RetrospectRepository retrospectRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private AnalyzeRepository analyzeRepository;
	@Autowired
	private MemberSpaceRelationRepository memberSpaceRelationRepository;

	@Autowired
	private AIAnalyzeService aiAnalyzeService;

	@SpyBean
	private OpenAIService openAIService;

	private String answer = "✅ KPT 회고 예시 (개인 개발 프로젝트 기준)\n"
		+ "\uD83D\uDFE2 Keep (잘한 것, 계속 유지할 점)\n"
		+ "매일 아침 스탠드업 미팅 전, 전날 작업을 간단히 정리하고 공유함 → 협업 효율이 높아짐\n"
		+ "\n"
		+ "커밋 메시지를 명확하게 작성하려고 노력함 → PR 리뷰 속도 향상\n"
		+ "\n"
		+ "API 응답 속도 개선을 위한 캐싱 전략(Caffeine 적용)을 시도하고 성공적으로 반영\n"
		+ "\n"
		+ "\uD83D\uDD34 Problem (문제였던 것, 아쉬운 점)\n"
		+ "기능 구현에 집중하다 보니 테스트 코드 작성이 미흡했음\n"
		+ "\n"
		+ "의사결정 과정에서 팀원과의 충분한 논의 없이 혼자 판단한 부분이 있었음\n"
		+ "\n"
		+ "일정 계획이 현실적이지 않아 마지막에 작업이 몰림\n"
		+ "\n"
		+ "\uD83D\uDFE1 Try (개선할 점, 새롭게 시도해볼 것)\n"
		+ "주요 기능 단위마다 테스트 코드를 먼저 작성하고 TDD에 가까운 흐름을 유지\n"
		+ "\n"
		+ "구현 전 간단한 설계/의논 시간을 미리 확보하고, 문서화도 함께 진행\n"
		+ "\n"
		+ "작업 계획을 세분화하고 매일 점검하여 일정 몰림을 방지";


	@Autowired
	private Time time;

	@Test
	void createAnalyze_shouldCreateAnalyzeAndUpdateStatus() {
		// given
		Long leaderId = 1L;
		Long memberId = 2L;

		// 0. 스페이스 생성
		Space space = SpaceFixture.createFixture(leaderId, 1L);
		Space savedSpace = spaceRepository.save(space);

		// 1. 테스트용 회고 데이터 생성
		Retrospect retrospect = RetrospectFixture.createFixture(savedSpace.getId(), RetrospectStatus.DONE,
			AnalysisStatus.PROCEEDING, time.now());
		retrospect = retrospectRepository.save(retrospect);

		// 2. 질문 등록
		Question rangeQuestion = QuestionFixture.create(1, QuestionType.RANGER);
		Question numberQuestion = QuestionFixture.create(2, QuestionType.NUMBER);
		Question textQuestion = QuestionFixture.create(3, QuestionType.PLAIN_TEXT);
		List<Question> questions = questionRepository.saveAll(List.of(rangeQuestion, numberQuestion, textQuestion));

		// 3. 팀원 및 답변 등록
		MemberSpaceRelation member1 = MemberSpaceRelationFixture.createFixture(space, leaderId);
		MemberSpaceRelation member2 = MemberSpaceRelationFixture.createFixture(space, memberId);
		memberSpaceRelationRepository.saveAll(List.of(member1, member2));

		Answer a1 = new Answer(retrospect.getId(), questions.get(0).getId(), leaderId, "80", AnswerStatus.DONE);
		Answer a2 = new Answer(retrospect.getId(), questions.get(1).getId(), leaderId, "4", AnswerStatus.DONE);
		Answer a3 = new Answer(retrospect.getId(), questions.get(2).getId(), leaderId, answer, AnswerStatus.DONE);

		Answer a4 = new Answer(retrospect.getId(), questions.get(0).getId(), memberId, "100", AnswerStatus.DONE);
		Answer a5 = new Answer(retrospect.getId(), questions.get(1).getId(), memberId, "2", AnswerStatus.DONE);
		Answer a6 = new Answer(retrospect.getId(), questions.get(2).getId(), leaderId, answer, AnswerStatus.DONE);
		answerRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6));

		// 4. OpenAI 응답 Mock 설정
		when(openAIService.createAnalyze(anyString()))
			.thenReturn(OpenAIResponseFixture.create()); // 테스트용 응답 객체

		AsyncAspect latch = new AsyncAspect();
		latch.init();

		// when
		aiAnalyzeService.createAnalyze(retrospect.getId());

		try{
			latch.await();
		}
		catch (Exception e){
			throw new InternalError();
		}

		// then
		List<Analyze> analyzes = analyzeRepository.findAll();
		assertThat(analyzes).hasSize(3);
		assertThat(analyzes.get(0).getAnalyzeType()).isEqualTo(AnalyzeType.TEAM);
		assertThat(analyzes.get(0).getScoreThree()).isEqualTo(1);
		assertThat(analyzes.get(0).getScoreFive()).isEqualTo(1);

		Retrospect updated = retrospectRepository.findById(retrospect.getId()).orElseThrow();
		assertThat(updated.getAnalysisStatus()).isEqualTo(AnalysisStatus.DONE);
		assertThat(updated.getRetrospectStatus()).isEqualTo(RetrospectStatus.DONE);
	}
}
