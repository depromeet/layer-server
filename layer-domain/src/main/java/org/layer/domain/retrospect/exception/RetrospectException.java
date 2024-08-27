package org.layer.domain.retrospect.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class RetrospectException extends BaseCustomException {
	public RetrospectException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
