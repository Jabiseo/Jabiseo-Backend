package com.jabiseo.certificate.exception;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.ErrorCode;

public class CertificateBusinessException extends BusinessException {

    public CertificateBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

}
