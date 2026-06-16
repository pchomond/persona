package com.pchomond.persona.exception;

import com.pchomond.persona.exception.core.ErrorCode;
import lombok.Getter;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Getter
public abstract class BaseAppException extends ErrorResponseException {

    private final ErrorCode errorCode;

    protected BaseAppException(ErrorCode errorCode, ProblemDetail problemDetail) {
        super(errorCode.getStatus(), problemDetail, null);
        this.errorCode = errorCode;
    }
}
