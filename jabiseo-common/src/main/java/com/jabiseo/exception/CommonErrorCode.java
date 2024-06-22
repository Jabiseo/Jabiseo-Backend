package com.jabiseo.exception;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode{

    INVALID_REQUEST_BODY("Invalid request body", "COM_001", ErrorCode.BAD_REQUEST),
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
