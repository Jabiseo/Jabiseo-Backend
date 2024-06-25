package com.jabiseo.problem.exception;

import com.jabiseo.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ProblemErrorCode implements ErrorCode {

    PROBLEM_NOT_FOUND("문제를 찾을 수 없습니다.", "PRO_001", ErrorCode.NOT_FOUND),
    INVALID_PROBLEM_COUNT("문제의 개수가 올바르지 않습니다.", "PRO_002", ErrorCode.BAD_REQUEST);


    private final String message;
    private final String errorCode;
    private final int statusCode;

    ProblemErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
