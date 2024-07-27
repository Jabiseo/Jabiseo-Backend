package com.jabiseo.problem.exception;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.ErrorCode;

public class ProblemBusinessException extends BusinessException {

    public ProblemBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

}
