package org.layer.storage.exception;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;

public class StorageException extends BaseCustomException {
    public StorageException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
