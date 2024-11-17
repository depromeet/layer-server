package org.layer.external.ncp.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class ExternalException extends BaseCustomException {
    public ExternalException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
