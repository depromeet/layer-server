package org.layer.domain.template.exception;

import lombok.RequiredArgsConstructor;
import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TemplateExceptionType implements ExceptionType {
    TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 템플릿입니다.");


    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public String message() {
        return null;
    }
}
