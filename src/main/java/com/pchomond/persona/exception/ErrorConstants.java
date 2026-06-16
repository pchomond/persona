package com.pchomond.persona.exception;

public final class ErrorConstants {

    private ErrorConstants() {}

    // The keys used in the ProblemDetail .setProperty() calls
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_ERROR_CODE = "error_code";
    public static final String KEY_ERRORS = "errors";

    public static final String KEY_CONFLICTING_RESOURCE = "conflicting_resource";
    public static final String KEY_CONFLICTING_FIELD = "conflicting_field";
    public static final String KEY_CONFLICTING_VALUE = "conflicting_value";

    // Example Business Error Codes
    public static final String CODE_DUPLICATE_USER = "ERR-USR-001";
    public static final String CODE_VALIDATION_FAILED = "ERR-VAL-000";
}
