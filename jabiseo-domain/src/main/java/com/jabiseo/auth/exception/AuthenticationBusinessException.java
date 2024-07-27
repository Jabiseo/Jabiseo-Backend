package com.jabiseo.auth.exception;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.ErrorCode;

public class AuthenticationBusinessException extends BusinessException {

    public AuthenticationBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
