package com.jabiseo.database.certificate.exception;

import com.jabiseo.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum CertificateErrorCode implements ErrorCode {

    CERTIFICATE_NOT_FOUND("Certificate not found", "CER_001", 404),

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
