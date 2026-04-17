package com.pchomond.persona.exception;

import org.openapitools.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String REQUEST_VALIDATION_EXCEPTION_TITLE = "Request Validation";
    private static final String BUSINESS_VALIDATION_EXCEPTION_TITLE = "Business Validation";

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<Error> handleRequestValidationException(RequestValidationException ex) {
        var error = Error.builder()
                .title(REQUEST_VALIDATION_EXCEPTION_TITLE)
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .details(ex.getErrorDetails())
                .build();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<Error> handleBusinessValidationException(BusinessValidationException ex) {
        var error = Error.builder()
                         .title(BUSINESS_VALIDATION_EXCEPTION_TITLE)
                         .status(HttpStatus.BAD_REQUEST.value())
                         .message(ex.getMessage())
                         .details(ex.getErrorDetails())
                         .build();
        return ResponseEntity.badRequest().body(error);
    }
}
