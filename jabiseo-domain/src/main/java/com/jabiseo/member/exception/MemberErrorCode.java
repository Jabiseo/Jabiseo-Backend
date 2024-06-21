package com.jabiseo.member.exception;

import com.jabiseo.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", "MEM_001", ErrorCode.NOT_FOUND),
    CERTIFICATE_STATE_NOT_FOUND("자격증 상태가 존재하지 않습니다.", "MEM_002", ErrorCode.NOT_FOUND),
    ;

    private final String message;
    private final String errorCode;
    private final int statusCode;

    MemberErrorCode(String message, String errorCode, int statusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
