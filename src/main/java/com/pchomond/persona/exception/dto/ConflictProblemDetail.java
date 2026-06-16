package com.pchomond.persona.exception.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pchomond.persona.exception.core.ConflictErrorCode;
import lombok.Getter;

@Getter
public class ConflictProblemDetail extends PersonaProblemDetail {

    @JsonProperty("conflicting_resource")
    private String conflictingResource;

    @JsonProperty("conflicting_field")
    private String conflictingField;

    @JsonProperty("conflicting_value")
    private String conflictingValue;

    public ConflictProblemDetail(ConflictErrorCode errorCode, String conflictingValue) {
        super(errorCode);
        this.conflictingResource = errorCode.getConflictingResource();
        this.conflictingField = errorCode.getConflictingField();
        this.conflictingValue = conflictingValue;
    }
}
