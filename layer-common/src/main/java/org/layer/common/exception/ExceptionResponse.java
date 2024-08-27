package org.layer.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ExceptionResponse {
    private String name;
    private String message;

    public static ExceptionResponse of(String name, String message) {
        return new ExceptionResponse(name, message);
    }
}