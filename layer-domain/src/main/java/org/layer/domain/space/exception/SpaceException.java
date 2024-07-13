package org.layer.domain.space.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class SpaceException extends BaseCustomException {
	public SpaceException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
