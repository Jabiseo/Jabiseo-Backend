package com.jabiseo.database.certificate.exception;

import com.jabiseo.exception.ErrorCode;
import com.jabiseo.database.exception.PersistenceException;

public class CertificateException extends PersistenceException {

    public CertificateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
