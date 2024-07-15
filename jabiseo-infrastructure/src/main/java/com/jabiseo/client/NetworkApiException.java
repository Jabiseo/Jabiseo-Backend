package com.jabiseo.client;

import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.ErrorCode;

public class NetworkApiException extends BusinessException {

    public NetworkApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
