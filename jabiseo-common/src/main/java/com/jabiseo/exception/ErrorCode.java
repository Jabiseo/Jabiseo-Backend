package com.jabiseo.exception;

public interface ErrorCode {
        int NOT_FOUND = 404;

        String getMessage();

        String getErrorCode();

        int getStatusCode();
}
