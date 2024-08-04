package com.jabiseo.exception;

import com.jabiseo.common.exception.BusinessException;
import com.jabiseo.common.exception.CommonErrorCode;
import com.jabiseo.common.exception.ErrorCode;
import com.jabiseo.database.exception.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
        ErrorCode errorCode = CommonErrorCode.INVALID_REQUEST_PARAMETER;
        String errorMessage;
        if (e.getDetailMessageArguments() == null || e.getDetailMessageArguments().length == 0) {
            errorMessage = errorCode.getMessage();
        } else {
            errorMessage = e.getDetailMessageArguments()[0].toString();
        }

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(new ErrorResponse(errorMessage, errorCode.getErrorCode()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        ErrorCode errorCode = CommonErrorCode.INVALID_REQUEST_PARAMETER;
        log.error(e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(new ErrorResponse(errorCode.getMessage(), errorCode.getErrorCode()));
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
        log.error(e.getMessage());
        String errorMessage = e.getMessage() +
                              " " +
                              CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage();
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(errorMessage, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode()));
    }

}
