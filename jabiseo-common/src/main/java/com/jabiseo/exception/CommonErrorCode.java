package com.jabiseo.exception;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {

    INVALID_REQUEST_BODY("요청 바디가 잘못됨", "COM_001", ErrorCode.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("서버 에러", "COM_002", ErrorCode.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST_PARAMETER("요청 파라미터가 잘못됨", "COM_003", ErrorCode.BAD_REQUEST),
    FORBIDDEN("권한이 없거나 금지된 요청임", "COM_004", ErrorCode.FORBIDDEN);

    private final String message;
    private final String errorCode;
    private final int statusCode;

    CommonErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

}
