package com.jabiseo.exception;

import com.jabiseo.common.exception.ErrorCode;

public record ErrorResponse(
        String message,
        String errorCode
) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getMessage(), errorCode.getErrorCode());
    }
}
