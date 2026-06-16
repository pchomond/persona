package com.pchomond.persona.exception.domain;

import com.pchomond.persona.exception.core.ErrorCode;
import java.net.URI;
import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements ErrorCode {
    VALIDATION_ERROR("GLOBAL_400_001", "The request failed validation checks.", HttpStatus.BAD_REQUEST, "urn:problem-type:validation-failed", "Request Validation Failed");

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;
    private final URI typeUri;
    private final String title;

    GlobalErrorCode(String code, String defaultMessage, HttpStatus status,  String typeUrn, String title) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
        this.typeUri = URI.create(typeUrn);
        this.title = title;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public URI getTypeUri() {
        return typeUri;
    }

    @Override
    public String getTitle() {
        return  title;
    }
}
