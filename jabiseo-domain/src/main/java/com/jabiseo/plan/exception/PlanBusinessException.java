package com.jabiseo.plan.exception;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.ErrorCode;

public class PlanBusinessException extends BusinessException {

    public PlanBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
