package com.pchomond.persona.exception;

import lombok.Getter;
import org.openapitools.model.ErrorDetail;

import java.util.List;

@Getter
public class RequestValidationException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Request validation failed";

    private final List<ErrorDetail> errorDetails;

    public RequestValidationException(List<ErrorDetail> errorDetails) {
        super(EXCEPTION_MESSAGE);
        this.errorDetails = errorDetails;
    }
}
