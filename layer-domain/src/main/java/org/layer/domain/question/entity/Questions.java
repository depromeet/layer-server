package org.layer.domain.question.entity;

import static org.layer.global.exception.QuestionExceptionType.*;

import java.util.List;

import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.exception.QuestionException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Questions {
	private final List<Question> questions;

	public void validateQuestionSize(int requestSize){
		if(questions.size() != requestSize){
			throw new QuestionException(INVALID_QUESTION_SIZE);
		}
	}

	public void validateIdAndQuestionType(Long questionId, QuestionType questionType){
		questions.stream()
			.filter(q -> q.getId().equals(questionId))
			.filter(q -> q.getQuestionType().equals(questionType))
			.findAny()
			.orElseThrow(() -> new QuestionException(INVALID_QUESTION));
	}

	public Long extractEssentialQuestionIdBy(QuestionType number) {
		return questions.stream()
			.filter(q -> q.getQuestionType().equals(number))
			.mapToLong(Question::getId)
			.findAny()
			.orElseThrow(() -> new QuestionException(NOT_INCLUDED_ESSENTIAL_QUESTION));
	}
}
