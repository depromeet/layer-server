package org.layer.domain.template.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class TemplateException extends BaseCustomException {
    public TemplateException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
