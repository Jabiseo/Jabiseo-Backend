package com.jabiseo.exception;

import com.jabiseo.database.exception.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode code = CommonErrorCode.INVALID_REQUEST;
        return ResponseEntity
                .status(code.getStatusCode())
                .body(new ErrorResponse(e.getMessage(), code.getErrorCode()));
    }
}
