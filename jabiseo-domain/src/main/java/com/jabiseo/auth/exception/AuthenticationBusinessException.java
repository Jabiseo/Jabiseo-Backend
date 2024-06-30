package com.jabiseo.auth.exception;

import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.ErrorCode;

public class AuthenticationBusinessException extends BusinessException {

    public AuthenticationBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
