package com.jabiseo.client;

import com.jabiseo.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum NetworkApiErrorCode implements ErrorCode {
    KAKAO_JWK_API_FAIL("카카오 kauth jwk 연결 실패", "NETWORK_001", ErrorCode.INTERNAL_SERVER_ERROR);

    private final String message;
    private final String errorCode;
    private final int statusCode;

    NetworkApiErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
