package com.jabiseo.exception;

public interface ErrorCode {

    int NOT_FOUND = 404;

    int BAD_REQUEST = 400;

    int UNAUTHORIZED = 401;

    int INTERNAL_SERVER_ERROR = 500;

    int FORBIDDEN = 403;

    String getMessage();

    String getErrorCode();

    int getStatusCode();
}
