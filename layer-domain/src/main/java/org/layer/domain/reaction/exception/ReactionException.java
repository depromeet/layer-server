package org.layer.domain.reaction.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class ReactionException extends BaseCustomException {
    public ReactionException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
