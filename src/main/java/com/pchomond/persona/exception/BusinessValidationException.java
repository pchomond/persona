package com.pchomond.persona.exception;

import lombok.Getter;
import org.openapitools.model.ErrorDetail;

import java.util.List;

@Getter
public class BusinessValidationException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Business validation failed";

    private final List<ErrorDetail> errorDetails;

    public BusinessValidationException(List<ErrorDetail> errorDetails) {
        super(EXCEPTION_MESSAGE);
        this.errorDetails = errorDetails;
    }
}
