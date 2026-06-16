package com.pchomond.persona.exception.domain;

import com.pchomond.persona.exception.core.ConflictErrorCode;
import java.net.URI;
import org.springframework.http.HttpStatus;

public enum UserConflictErrorCode implements ConflictErrorCode {

    EMAIL_ALREADY_EXISTS("Email Already Exists", "USER_409_001", "A user account with this email address already exists.", "User", "email", "urn:problem-type:email-already-exists"),
    IDP_ID_ALREADY_EXISTS("IdP Id Already Exists", "USER_409_002", "This Identity Provider ID is already linked to another user account.", "User", "idp_id", "urn:problem-type:idp-id-already-exists");

    private final String title;
    private final String code;
    private final String defaultMessage;
    private final String resource;
    private final String field;
    private final URI typeUri;

    UserConflictErrorCode(String title, String code, String defaultMessage, String resource, String field, String typeUri) {
        this.title = title;
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.resource = resource;
        this.field = field;
        this.typeUri = URI.create(typeUri);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getConflictingResource() {
        return resource;
    }

    @Override
    public String getConflictingField() {
        return field;
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
        return HttpStatus.CONFLICT;
    }

    @Override
    public URI getTypeUri() {
        return typeUri;
    }

}
