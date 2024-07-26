package com.jabiseo.exception;

import com.jabiseo.database.exception.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException e) {
        ErrorCode code = e.getErrorCode();
        return ResponseEntity
                .status(code.getStatusCode())
                .body(new ErrorResponse(code.getMessage(), code.getErrorCode()));
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<?> handlePersistenceException(PersistenceException e) {
        ErrorCode code = e.getErrorCode();
        return ResponseEntity
                .status(code.getStatusCode())
                .body(new ErrorResponse(code.getMessage(), code.getErrorCode()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleMethodValidationException(HandlerMethodValidationException e) {
        ErrorCode errorCode = CommonErrorCode.INVALID_REQUEST_BODY;
        log.error(e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(new ErrorResponse(e.getMessage(), errorCode.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(e.getMessage());
        stringBuilder.append(CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(stringBuilder.toString(), CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode()));
    }

}
