package com.jabiseo.plan.exception;

import com.jabiseo.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum PlanErrorCode implements ErrorCode {

    ALREADY_EXIST_PLAN("이미 진행중인 플랜이 있습니다","PLAN_001", ErrorCode.CONFLICT);

    PlanErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    private final String message;
    private final String errorCode;
    private final int statusCode;
}
