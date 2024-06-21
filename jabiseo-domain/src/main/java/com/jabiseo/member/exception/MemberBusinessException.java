package com.jabiseo.member.exception;

import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.ErrorCode;

public class MemberBusinessException extends BusinessException {

        public MemberBusinessException(ErrorCode errorCode) {
            super(errorCode);
        }

}
