package org.layer.domain.notice.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class NoticeException extends BaseCustomException {
    public NoticeException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
