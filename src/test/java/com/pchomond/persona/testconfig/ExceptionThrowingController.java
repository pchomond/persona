package com.pchomond.persona.testconfig;

import static com.pchomond.persona.exception.domain.UserConflictErrorCode.EMAIL_ALREADY_EXISTS;
import static com.pchomond.persona.validation.UserValidationConstants.FIELD_EMAIL;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_INVALID_EMAIL_FORMAT;

import com.pchomond.persona.exception.RequestValidationException;
import com.pchomond.persona.exception.ResourceConflictException;
import jakarta.validation.Valid;
import java.util.List;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.FieldViolation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-exceptions")
class ExceptionThrowingController {

    @GetMapping("/resource-conflict")
    public void throwResourceConflict() {
        throw new ResourceConflictException(EMAIL_ALREADY_EXISTS, "clash@domain.com");
    }

    @GetMapping("/request-validation")
    public void throwRequestValidation() {
        throw new RequestValidationException(List.of(FieldViolation.builder()
                .field(FIELD_EMAIL).message(MSG_INVALID_EMAIL_FORMAT).build()));
    }

    @PostMapping("/spring-validation")
    public void throwSpringValidation(@Valid @RequestBody CreateUserRequest request) {
        // Will not be reached if @Valid fails
    }
}
