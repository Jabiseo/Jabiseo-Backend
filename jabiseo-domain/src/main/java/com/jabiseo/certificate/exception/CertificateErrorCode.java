package com.jabiseo.certificate.exception;

import com.jabiseo.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum CertificateErrorCode implements ErrorCode {


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
