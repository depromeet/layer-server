package org.layer.domain.member.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class MemberException extends BaseCustomException {
	public MemberException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
