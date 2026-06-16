package com.pchomond.persona.exception.core;

import java.net.URI;
import org.springframework.http.HttpStatus;

/**
 * The foundational contract for all business errors in the system.
 */
public interface ErrorCode {
    /**
     * The machine-readable string sent in the "error_code" field of ProblemDetail.
     */
    String getCode();

    /**
     * The default localized/fallback message used in the "detail" field.
     */
    String getDefaultMessage();

    /**
     * The HTTP status code natively coupled to this specific domain scenario.
     */
    HttpStatus getStatus();

    /**
     * The unique URI pointer used in the "type" field of ProblemDetail.
     */
    URI getTypeUri();

    /**
     * The error cause used in the "title" field of ProblemDetail.
     */
    String getTitle();
}
