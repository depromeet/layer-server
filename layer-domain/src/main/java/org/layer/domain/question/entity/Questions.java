package org.layer.domain.question.entity;

import java.util.List;

import org.layer.common.exception.BaseCustomException;
import org.layer.domain.question.enums.QuestionType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Questions {
	private final List<Question> questions;

	public void validateQuestionSize(int requestSize){
		if(questions.size() != requestSize){
			throw new BaseCustomException(null);  // TODO: 수정 필요
		}
	}

	public void validateIdAndQuestionType(Long questionId, QuestionType questionType){
		questions.stream()
			.filter(q -> q.getId().equals(questionId))
			.filter(q -> q.getQuestionType().equals(questionType))
			.findAny()
			.orElseThrow(() -> new BaseCustomException(null));  // TODO: 수정 필요
	}
}
