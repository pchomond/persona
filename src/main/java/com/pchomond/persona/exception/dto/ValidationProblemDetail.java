package com.pchomond.persona.exception.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pchomond.persona.exception.core.ErrorCode;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import com.pchomond.persona.api.rest.model.FieldViolation;

@Getter
public class ValidationProblemDetail extends PersonaProblemDetail{

    @JsonProperty("errors")
    private final List<FieldViolation> errors;

    public ValidationProblemDetail(ErrorCode errorCode, List<FieldViolation> errors) {
        super(errorCode);

        this.errors = errors != null ? Collections.unmodifiableList(errors) : List.of();
    }
}
