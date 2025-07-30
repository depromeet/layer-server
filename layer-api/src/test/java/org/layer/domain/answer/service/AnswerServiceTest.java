package org.layer.domain.answer.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.layer.domain.answer.controller.dto.request.AnswerCreateRequest;
import org.layer.domain.answer.controller.dto.request.AnswerListCreateRequest;
import org.layer.domain.answer.controller.dto.response.AnswerListGetResponse;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AnswerServiceTest {

	@Autowired
	private AnswerService answerService;

	@Autowired
	private RetrospectRepository retrospectRepository;

	@Nested
	@SqlGroup({
		@Sql(value = "/sql/answer-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

	})
	class 회고_답변_조회 {

		@Test
		@DisplayName("특정 회고에 답변하지 않은 유저도 완료된 해당 회고를 조회할 수 있다.")
		void getAnalyzeAnswersTest_1() {
			// given
			Long spaceId = 1L;
			Long retrospectId = 1L;
			Long memberId = 2L;

			// when
			AnswerListGetResponse analyzeAnswers = answerService.getAnalyzeAnswers(spaceId, retrospectId, memberId);

			// then
			assertThat(analyzeAnswers).isNotNull();
			assertThat(analyzeAnswers.questions()).hasSize(4);
			assertThat(analyzeAnswers.questions().get(0).questionContent()).isEqualTo("질문1");
			assertThat(analyzeAnswers.questions().get(0).answers().get(0).answerContent()).isEqualTo("5");
			assertThat(analyzeAnswers.questions().get(1).answers().get(0).answerContent()).isEqualTo("80");

			assertThat(analyzeAnswers.individuals()).hasSize(1);
			assertThat(analyzeAnswers.individuals().get(0).name()).isEqualTo("홍길동");
			assertThat(analyzeAnswers.individuals().get(0).answers().get(0).answerContent()).isEqualTo("5");
			assertThat(analyzeAnswers.individuals().get(0).answers().get(1).answerContent()).isEqualTo("80");
		}
	}

	@Nested
	@SqlGroup({
		@Sql(value = "/sql/answer-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

	})
	class 회고_작성 {
		@Test
		@DisplayName("특정 회고 작성 후에 작성된 답변이 팀 인원 수와 같다면, 회고 마감 상태로 변경된다."
			+ "또한 만약 마감기한이 미지정이라면, 마감기한이 현재 시간으로 설정된다.")
		void createAnswerTest_1() {
			// given
			Long spaceId = 1L;
			Long retrospectId = 1L;
			Long memberId = 2L;

			AnswerCreateRequest request1 = new AnswerCreateRequest(
				1L, "number", "5"
			);

			AnswerCreateRequest request2 = new AnswerCreateRequest(
				2L, "range", "100"
			);

			AnswerCreateRequest request3 = new AnswerCreateRequest(
				3L, "plain_text", "답변1"
			);

			AnswerCreateRequest request4 = new AnswerCreateRequest(
				4L, "plain_text", "답변2"
			);

			// when
			answerService.create(new AnswerListCreateRequest(
				List.of(request1, request2, request3, request4), false),
				spaceId, retrospectId, memberId);

			// then
			Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
			assertThat(retrospect.getRetrospectStatus()).isEqualTo(RetrospectStatus.DONE);
			assertThat(retrospect.getDeadline()).isNotNull();
		}
	}

}
