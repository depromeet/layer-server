package org.layer.oauth.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class OAuthException extends BaseCustomException {
    public OAuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}