package com.pchomond.persona.exception;

import com.pchomond.persona.exception.core.ConflictErrorCode;
import com.pchomond.persona.exception.dto.ConflictProblemDetail;
import lombok.Getter;

@Getter
public class ResourceConflictException extends BaseAppException {

    private final String conflictingValue;

    public ResourceConflictException(ConflictErrorCode errorCode, String value) {
        ConflictProblemDetail detail = new ConflictProblemDetail(errorCode, value);
        super(errorCode, detail);

        this.conflictingValue = value;
    }

    /**
     * Convenience method to extract the field name natively without parsing the JSON body.
     */
    public String getConflictingField() {
        return ((ConflictErrorCode) this.getErrorCode()).getConflictingField();
    }

    /**
     * Convenience method to extract the resource name natively without parsing the JSON body.
     */
    public String getConflictingResource() {
        return ((ConflictErrorCode) this.getErrorCode()).getConflictingResource();
    }
}
