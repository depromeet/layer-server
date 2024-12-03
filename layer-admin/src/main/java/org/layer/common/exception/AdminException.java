package org.layer.common.exception;

public class AdminException extends BaseCustomException {
    public AdminException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
