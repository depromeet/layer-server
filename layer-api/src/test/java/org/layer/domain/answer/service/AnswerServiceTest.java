package org.layer.domain.answer.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.layer.domain.answer.controller.dto.response.AnswerListGetResponse;
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

	@Nested
	@SqlGroup({
		@Sql(value = "/sql/answer-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

	})
	class 회고_답변_조회 {

		@Test
		@DisplayName("특정 회고에 답변하지 않은 유저도 완료된 해당 회고를 조회할 수 있다.")
		void test1() {
			// given
			Long spaceId = 1L;
			Long retrospectId = 1L;
			Long memberId = 2L;

			// when
			AnswerListGetResponse analyzeAnswers = answerService.getAnalyzeAnswers(spaceId, retrospectId, memberId);

			// then
			assertThat(analyzeAnswers).isNotNull();
			assertThat(analyzeAnswers.questions()).hasSize(2);
			assertThat(analyzeAnswers.questions().get(0).questionContent()).isEqualTo("질문1");
			assertThat(analyzeAnswers.questions().get(0).answers().get(0).answerContent()).isEqualTo("회고답변 1");
			assertThat(analyzeAnswers.questions().get(1).answers().get(0).answerContent()).isEqualTo("회고답변 2");

			assertThat(analyzeAnswers.individuals()).hasSize(1);
			assertThat(analyzeAnswers.individuals().get(0).name()).isEqualTo("홍길동");
			assertThat(analyzeAnswers.individuals().get(0).answers().get(0).answerContent()).isEqualTo("회고답변 1");
			assertThat(analyzeAnswers.individuals().get(0).answers().get(1).answerContent()).isEqualTo("회고답변 2");
		}
	}

}
