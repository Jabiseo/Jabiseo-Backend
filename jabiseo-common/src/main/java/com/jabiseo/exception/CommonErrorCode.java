package com.jabiseo.exception;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {

    INVALID_REQUEST("Invalid request", "COM_001", ErrorCode.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("server error", "COM_002", ErrorCode.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final String errorCode;
    private final int statusCode;

    CommonErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

}
