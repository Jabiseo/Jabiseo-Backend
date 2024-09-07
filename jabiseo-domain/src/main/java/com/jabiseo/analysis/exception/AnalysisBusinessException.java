package com.jabiseo.analysis.exception;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.ErrorCode;

public class AnalysisBusinessException extends BusinessException {

    public AnalysisBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

}
