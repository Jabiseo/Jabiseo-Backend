package com.jabiseo.certificate.exception;

import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.ErrorCode;

public class CertificateBusinessException extends BusinessException {

    public CertificateBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

}
