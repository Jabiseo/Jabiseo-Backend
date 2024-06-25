package com.jabiseo.problem.exception;

import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.ErrorCode;

public class ProblemBusinessException extends BusinessException {

    public ProblemBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

}
