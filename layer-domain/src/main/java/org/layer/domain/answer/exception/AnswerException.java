package org.layer.domain.answer.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class AnswerException extends BaseCustomException {
	public AnswerException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
