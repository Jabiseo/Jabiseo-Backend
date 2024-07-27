package com.jabiseo.client;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.ErrorCode;

public class NetworkApiException extends BusinessException {

    public NetworkApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
