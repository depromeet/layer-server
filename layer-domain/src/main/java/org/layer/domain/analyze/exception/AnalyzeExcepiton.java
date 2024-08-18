package org.layer.domain.analyze.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class AnalyzeExcepiton extends BaseCustomException {

	public AnalyzeExcepiton(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
