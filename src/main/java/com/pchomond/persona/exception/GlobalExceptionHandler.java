package com.pchomond.persona.exception;

import static com.pchomond.persona.exception.domain.GlobalErrorCode.VALIDATION_ERROR;

import com.pchomond.persona.exception.dto.ValidationProblemDetail;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.openapitools.model.FieldViolation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        ValidationProblemDetail pd = new ValidationProblemDetail(
                VALIDATION_ERROR,
                extractFieldViolations(ex.getBindingResult())
        );

        return handleExceptionInternal(ex, pd, headers, status, request);
    }

    private static List<FieldViolation> extractFieldViolations(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> new FieldViolation(error.getField(), error.getDefaultMessage()))
                .toList();
    }
}
