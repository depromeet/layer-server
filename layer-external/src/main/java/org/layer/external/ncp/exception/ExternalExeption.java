package org.layer.external.ncp.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class ExternalExeption extends BaseCustomException {
    public ExternalExeption(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
