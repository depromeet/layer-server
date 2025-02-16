package org.layer.domain.admin.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class AdminException extends BaseCustomException {
    public AdminException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
