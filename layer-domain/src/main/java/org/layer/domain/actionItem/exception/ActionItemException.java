package org.layer.domain.actionItem.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class ActionItemException extends BaseCustomException {
    public ActionItemException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
