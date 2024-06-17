package com.jabiseo.certificate.exception;

import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.ErrorCode;

public class CertificateException extends BusinessException {

    public CertificateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
