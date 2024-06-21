package com.jabiseo.certificate.exception;

import com.jabiseo.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum CertificateErrorCode implements ErrorCode {

    CERTIFICATE_NOT_FOUND("자격증을 찾을 수 없습니다.", "CER_001", ErrorCode.NOT_FOUND),
    ;


    private final String message;
    private final String errorCode;
    private final int statusCode;

    CertificateErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
