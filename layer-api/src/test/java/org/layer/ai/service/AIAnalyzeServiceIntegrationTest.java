package org.layer.ai.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("test")
@EnableAsync
@Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Slf4j
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

	@Autowired
	private Time time;

	@Autowired
	private TestConfig.AsyncAspect asyncAspect;

	@TestConfiguration
	static class TestConfig {

		@Aspect
		@Component
		static class AsyncAspect {

			private CountDownLatch countDownLatch;

			public void init() {
				countDownLatch = new CountDownLatch(1);
			}

			@After("execution(* org.layer.ai.service.AIAnalyzeService.createAnalyze(*))")
			public void afterIcalendarCreation() {
				log.info("비동기 메서드 실행 감지됨: createAnalyze 종료됨");
				countDownLatch.countDown();
			}

			public void await() throws InterruptedException {
				log.info("CountDownLatch 대기 시작");
				countDownLatch.await();
				log.info("CountDownLatch 완료, 약간의 대기 추가");
				Thread.sleep(10); // 안정성을 위한 짧은 지연
			}
		}
	}

	@Test
	void createAnalyze_shouldCreateAnalyzeAndUpdateStatus() throws InterruptedException {
		// given
		Long leaderId = 1L;
		Long memberId = 2L;
		String answer = "너무 재밌었어요.";

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

		log.info("AsyncAspect 초기화");
		asyncAspect.init();

		// when
		log.info("비동기 분석 실행 시작");
		aiAnalyzeService.createAnalyze(retrospect.getId());
		asyncAspect.await();
		log.info("비동기 분석 실행 완료");

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
