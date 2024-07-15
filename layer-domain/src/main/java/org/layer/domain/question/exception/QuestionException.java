package org.layer.domain.question.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class QuestionException extends BaseCustomException {
	public QuestionException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
