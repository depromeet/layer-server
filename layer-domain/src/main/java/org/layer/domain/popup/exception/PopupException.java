package org.layer.domain.popup.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class PopupException extends BaseCustomException {
    public PopupException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
