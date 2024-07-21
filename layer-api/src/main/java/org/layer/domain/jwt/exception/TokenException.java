package org.layer.domain.jwt.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class TokenException extends BaseCustomException {
    public TokenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}