package com.pchomond.persona.exception.core;

/**
 * Specialized contract for 409 Conflict errors.
 */
public interface ConflictErrorCode extends ErrorCode {
    /**
     * Maps to "conflicting_resource" in the error response.
     */
    String getConflictingResource();

    /**
     * Maps to "conflicting_field" in the error response.
     */
    String getConflictingField();
}
