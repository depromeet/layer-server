package org.layer.external.ai.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class OpenAIException extends BaseCustomException {
	public OpenAIException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
