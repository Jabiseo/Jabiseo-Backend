package com.jabiseo.exception;

import com.jabiseo.exception.ErrorCode;
import lombok.Getter;

@Getter
public class PersistenceException extends RuntimeException {
    private final ErrorCode errorCode;

    public PersistenceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
