package org.layer.domain.fixture;

import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.enums.QuestionType;

public class QuestionFixture {
	public static Question create(int questionOrder, QuestionType questionType) {
		return Question.builder()
			.retrospectId(1L)
			.content("질문1")
			.questionOwner(QuestionOwner.TEAM)
			.questionType(questionType)
			.questionOrder(questionOrder)
			.build();
	}
}
