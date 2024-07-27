package com.jabiseo.member.exception;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.ErrorCode;

public class MemberBusinessException extends BusinessException {

        public MemberBusinessException(ErrorCode errorCode) {
            super(errorCode);
        }

}
