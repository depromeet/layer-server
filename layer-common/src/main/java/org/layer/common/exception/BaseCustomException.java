package org.layer.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseCustomException extends RuntimeException {
    private final ExceptionType exceptionType;

    @Override
    public String getMessage() {
        return exceptionType.message();
    }
}
