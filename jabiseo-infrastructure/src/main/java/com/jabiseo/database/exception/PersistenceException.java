package com.jabiseo.database.exception;

import com.jabiseo.exception.ErrorCode;
import lombok.Getter;

@Getter
public class PersistenceException extends RuntimeException {
    private final ErrorCode errorCode;

    public PersistenceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
