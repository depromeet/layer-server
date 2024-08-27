package org.layer.domain.jwt.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class AuthException extends BaseCustomException {
    public AuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
