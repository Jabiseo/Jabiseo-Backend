package com.jabiseo.exception;

public interface ErrorCode {

        int NOT_FOUND = 404;

        int BAD_REQUEST = 400;

        String getMessage();

        String getErrorCode();

        int getStatusCode();
}
