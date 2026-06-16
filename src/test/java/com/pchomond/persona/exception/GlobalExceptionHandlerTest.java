package com.pchomond.persona.exception;

import static com.pchomond.persona.exception.domain.GlobalErrorCode.VALIDATION_ERROR;
import static com.pchomond.persona.exception.domain.UserConflictErrorCode.EMAIL_ALREADY_EXISTS;
import static com.pchomond.persona.validation.UserValidationConstants.FIELD_EMAIL;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_INVALID_EMAIL_FORMAT;

import com.pchomond.persona.testconfig.EnableExceptionDummyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.client.RestTestClient;

@EnableExceptionDummyController
@AutoConfigureRestTestClient
public class GlobalExceptionHandlerTest {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    @DisplayName("Should return error response when a request validation exception occurs")
    void shouldReturnErrorWhenRequestValidationExceptionIsThrown() {
        // when & then
        restTestClient.get()
                .uri("/test-exceptions/request-validation")
                .accept(MediaType.APPLICATION_PROBLEM_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath("$.title").isEqualTo(VALIDATION_ERROR.getTitle())
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.error_code").isEqualTo(VALIDATION_ERROR.getCode())
                .jsonPath("$.errors").isArray()
                .jsonPath("$.errors[0].field").isEqualTo(FIELD_EMAIL)
                .jsonPath("$.errors[0].message").isEqualTo(MSG_INVALID_EMAIL_FORMAT);
    }

    @Test
    @DisplayName("Should return error response when a resource conflict exception occurs")
    void shouldReturnErrorWhenResourceConflictExceptionIsThrown() {
        // when & then
        restTestClient.get()
                .uri("/test-exceptions/resource-conflict")
                .accept(MediaType.APPLICATION_PROBLEM_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath("$.title").isEqualTo(EMAIL_ALREADY_EXISTS.getTitle())
                .jsonPath("$.status").isEqualTo(409)
                .jsonPath("$.type").isEqualTo(EMAIL_ALREADY_EXISTS.getTypeUri().toString())
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.error_code").isEqualTo(EMAIL_ALREADY_EXISTS.getCode())
                .jsonPath("$.conflicting_resource").isEqualTo("User")
                .jsonPath("$.conflicting_field").isEqualTo("email")
                .jsonPath("$.conflicting_value").isEqualTo("clash@domain.com");
    }

    @Test
    @DisplayName("Should return error response when a method argument not valid exception occurs")
    void shouldReturnErrorWhenMethodArgumentNotValidExceptionIsThrown() {
        restTestClient.post()
                .uri("/test-exceptions/spring-validation")
                .contentType(MediaType.APPLICATION_JSON)
                .body("{ \"email\": \"email@gmail.com\" }")
                .accept(MediaType.APPLICATION_PROBLEM_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody()
                .jsonPath("$.error_code").isEqualTo(VALIDATION_ERROR.getCode())
                .jsonPath("$.title").isEqualTo(VALIDATION_ERROR.getTitle())
                .jsonPath("$.errors").isArray();
    }

    @Test
    @DisplayName("Should return error response when a native Spring malformed JSON exception occurs")
    void shouldReturnErrorWhenSpringNativeExceptionsAreThrown() {
        restTestClient.post()
                .uri("/test-exceptions/spring-validation")
                .contentType(MediaType.APPLICATION_JSON)
                // Intentionally sending malformed JSON (missing closing brace)
                .body("{ \"email\": \"badformat\" ")
                .accept(MediaType.APPLICATION_PROBLEM_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody()
                .jsonPath("$.error_code").doesNotExist()
                .jsonPath("$.title").isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }
}
