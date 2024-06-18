package com.jabiseo.certificate.exception;

import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.ErrorCode;

public class CertificatePersistenceException extends BusinessException {

    public CertificatePersistenceException(ErrorCode errorCode) {
        super(errorCode);
    }

}
