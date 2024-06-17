package com.jabiseo.exception;

public record ErrorResponse(
        String message,
        String errorCode
) {

}
