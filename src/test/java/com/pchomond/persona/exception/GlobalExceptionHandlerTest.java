package com.pchomond.persona.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Error;
import org.openapitools.model.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private static final String FIELD_TEST = "field.test";
    private static final String MSG_INVALID_ERROR = "This field is invalid";

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleRequestValidationException_shouldReturnError_whenRequestValidationException() {
        // given
        RequestValidationException exception = createRequestValidationException();

        // when
        ResponseEntity<Error> response = globalExceptionHandler.handleRequestValidationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();

        Error errorBody = response.getBody();
        assertThat(errorBody.getStatus()).isEqualTo(400);
        assertThat(errorBody.getTitle()).isEqualTo("Request Validation");
        assertThat(errorBody.getMessage()).isEqualTo("Request validation failed");
        assertThat(errorBody.getDetails())
                .hasSize(1)
                .containsExactlyElementsOf(exception.getErrorDetails());
    }

    @Test
    void handleRequestValidationException_shouldReturnError_whenBusinessValidationException() {
        // given
        BusinessValidationException exception = createBusinessValidationException();

        // when
        ResponseEntity<Error> response = globalExceptionHandler.handleBusinessValidationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();

        Error errorBody = response.getBody();
        assertThat(errorBody.getStatus()).isEqualTo(400);
        assertThat(errorBody.getTitle()).isEqualTo("Business Validation");
        assertThat(errorBody.getMessage()).isEqualTo("Business validation failed");
        assertThat(errorBody.getDetails())
                .hasSize(1)
                .containsExactlyElementsOf(exception.getErrorDetails());
    }

    private static RequestValidationException createRequestValidationException() {
        return new RequestValidationException(
                List.of(ErrorDetail.builder()
                                .field(FIELD_TEST)
                                .reason(MSG_INVALID_ERROR)
                                .build())
        );
    }

    private static BusinessValidationException createBusinessValidationException() {
        return new BusinessValidationException(
                List.of(ErrorDetail.builder()
                                   .field(FIELD_TEST)
                                   .reason(MSG_INVALID_ERROR)
                                   .build())
        );
    }
}
