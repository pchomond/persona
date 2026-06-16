package com.pchomond.persona.exception.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pchomond.persona.exception.core.ErrorCode;
import java.time.Instant;
import lombok.Getter;
import org.springframework.http.ProblemDetail;

@Getter
public abstract class PersonaProblemDetail extends ProblemDetail {

    @JsonProperty("error_code")
    private final String errorCode;

    @JsonProperty("timestamp")
    private final String timestamp;

    protected PersonaProblemDetail(ErrorCode errorCode) {
        super(errorCode.getStatus().value());

        // RFC 9457 fields
        this.setDetail(errorCode.getDefaultMessage());
        this.setType(errorCode.getTypeUri());
        this.setTitle(errorCode.getTitle());

        // Custom baseline fields
        this.errorCode = errorCode.getCode();
        this.timestamp = Instant.now().toString();
    }
}
