package org.layer.domain.space.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class MemberSpaceRelationException extends BaseCustomException {
	public MemberSpaceRelationException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
