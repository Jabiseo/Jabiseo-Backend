package com.jabiseo.analysis.exception;

import com.jabiseo.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum AnalysisErrorCode implements ErrorCode {

    CANNOT_CALCULATE_VULNERABILITY("취약점 분석을 할 수 없습니다.", "LRN_001", ErrorCode.INTERNAL_SERVER_ERROR),
    CANNOT_FIND_VECTOR("벡터를 찾을 수 없습니다.", "LRN_002", ErrorCode.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final String errorCode;
    private final int statusCode;

    AnalysisErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
