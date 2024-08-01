package org.layer.domain.form.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class FormException extends BaseCustomException {
	public FormException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
