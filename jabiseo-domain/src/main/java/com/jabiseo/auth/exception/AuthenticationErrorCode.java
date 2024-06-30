package com.jabiseo.auth.exception;

import com.jabiseo.exception.ErrorCode;

public enum AuthenticationErrorCode implements ErrorCode {

    INVALID_ID_TOKEN("잘못된 idToken 입니다.", "AUTH_001", ErrorCode.AUTHENTICATION_ERROR),
    EXPIRED_ID_TOKEN("만료된 idToken 입니다.", "AUTH_002", ErrorCode.AUTHENTICATION_ERROR),
    NOT_SUPPORT_OAUTH("지원하지 않는 oauth 인증 수단입니다", "AUTH_003", ErrorCode.AUTHENTICATION_ERROR);

    private final String message;
    private final String errorCode;
    private final int statusCode;

    AuthenticationErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }


    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }
}
