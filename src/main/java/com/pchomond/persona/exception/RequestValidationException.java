package com.pchomond.persona.exception;

import static com.pchomond.persona.exception.domain.GlobalErrorCode.VALIDATION_ERROR;

import com.pchomond.persona.exception.dto.ValidationProblemDetail;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import com.pchomond.persona.api.rest.model.FieldViolation;

@Getter
public class RequestValidationException extends BaseAppException {

    private final List<FieldViolation> errors;

    public RequestValidationException(List<FieldViolation> errors) {
        super(VALIDATION_ERROR, new ValidationProblemDetail(VALIDATION_ERROR, errors));

        this.errors = errors != null ? Collections.unmodifiableList(errors) : List.of();
    }
}
