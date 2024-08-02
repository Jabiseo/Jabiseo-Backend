package com.jabiseo.exception;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.CommonErrorCode;
import com.jabiseo.common.exception.ErrorCode;
import com.jabiseo.database.exception.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;
import java.util.stream.Collectors;

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
        ErrorCode errorCode = CommonErrorCode.INVALID_REQUEST_PARAMETER;
        log.error(e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(new ErrorResponse(e.getMessage(), errorCode.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = CommonErrorCode.INVALID_REQUEST_PARAMETER;
        log.error(e.getMessage());
        StringBuilder errors = new StringBuilder();
        e.getBindingResult()
                .getFieldErrors()
                .forEach((er) -> errors.append(er.getDefaultMessage()));

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(new ErrorResponse(errors.toString(), errorCode.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        StringBuilder errorMessage = new StringBuilder();
        System.out.println();
        log.error(e.getMessage());
        errorMessage.append(e.getMessage())
                .append(" ")
                .append(CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(errorMessage.toString(), CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode()));
    }

}
