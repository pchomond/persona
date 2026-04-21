package com.pchomond.persona.api.validation;

import com.pchomond.persona.exception.RequestValidationException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Address;
import org.openapitools.model.BirthDate;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.ErrorDetail;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static com.pchomond.persona.validation.UserValidationConstants.FIELD_DOB;
import static com.pchomond.persona.validation.UserValidationConstants.FIELD_DOB_DAY;
import static com.pchomond.persona.validation.UserValidationConstants.FIELD_DOB_MONTH;
import static com.pchomond.persona.validation.UserValidationConstants.FIELD_DOB_YEAR;
import static com.pchomond.persona.validation.UserValidationConstants.FIELD_EMAIL;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_INVALID_DAY;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_INVALID_DOB;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_INVALID_EMAIL_FORMAT;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_INVALID_MONTH;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_INVALID_YEAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.as;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserRequestValidatorTest {

    @Mock
    private EmailValidator emailValidator;

    private UserRequestValidator userRequestValidator;

    @BeforeEach
    public void init() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-04-17T10:00:00Z"), ZoneOffset.UTC);
        userRequestValidator = new UserRequestValidator(emailValidator, fixedClock);
    }

    @Test
    void validate_shouldReturnEmptyList_whenNonErrorsOccur() {
        // given
        CreateUserRequest request = generateCreateUserRequest();
        given(emailValidator.isValid(eq(request.getEmail()), any())).willReturn(true);

        // when & then
        assertThatNoException().isThrownBy(() -> userRequestValidator.validate(request));

        verify(emailValidator).isValid(eq(request.getEmail()), any());
    }

    @Test
    void validate_shouldReturnError_whenEmailIsInvalid() {
        // given
        CreateUserRequest request = generateCreateUserRequest();
        given(emailValidator.isValid(eq(request.getEmail()), any())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userRequestValidator.validate(request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("Request validation failed")
                .extracting("errorDetails", as(InstanceOfAssertFactories.LIST))
                .hasSize(1)
                .anySatisfy(error -> {
                    ErrorDetail detail = (ErrorDetail) error;
                    assertThat(detail.getField()).isEqualTo(FIELD_EMAIL);
                    assertThat(detail.getReason()).isEqualTo(MSG_INVALID_EMAIL_FORMAT);
                });

        verify(emailValidator).isValid(eq(request.getEmail()), any());
    }

    @Test
    void validate_shouldReturnErrors_whenDobFieldsAreInvalid() {
        // given
        CreateUserRequest request = generateCreateUserRequest();
        var invalidDob = BirthDate.builder().year(1919).month(13).day(0).build();
        request.setDateOfBirth(invalidDob);
        given(emailValidator.isValid(eq(request.getEmail()), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userRequestValidator.validate(request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("Request validation failed")
                .extracting("errorDetails", as(InstanceOfAssertFactories.LIST))
                .hasSize(3)
                .extracting("field", "reason")
                .containsExactlyInAnyOrder(
                        tuple(FIELD_DOB_YEAR, MSG_INVALID_YEAR),
                        tuple(FIELD_DOB_MONTH, MSG_INVALID_MONTH),
                        tuple(FIELD_DOB_DAY, MSG_INVALID_DAY));

        verify(emailValidator).isValid(eq(request.getEmail()), any());
    }

    @Test
    void validate_shouldReturnError_whenDobDoesNotExist() {
        // given
        CreateUserRequest request = generateCreateUserRequest();
        var invalidDob = BirthDate.builder().year(2024).month(4).day(31).build();
        request.setDateOfBirth(invalidDob);
        given(emailValidator.isValid(eq(request.getEmail()), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userRequestValidator.validate(request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("Request validation failed")
                .extracting("errorDetails", as(InstanceOfAssertFactories.LIST))
                .hasSize(1)
                .anySatisfy(error -> {
                    ErrorDetail detail = (ErrorDetail) error;
                    assertThat(detail.getField()).isEqualTo(FIELD_DOB);
                    assertThat(detail.getReason()).isEqualTo(MSG_INVALID_DOB);
                });

        verify(emailValidator).isValid(eq(request.getEmail()), any());
    }

    private static CreateUserRequest generateCreateUserRequest() {
        return CreateUserRequest.builder()
                .idpId(UUID.randomUUID().toString())
                .email("test@gmail.com")
                .givenName("Test")
                .surname("User")
                .dateOfBirth(BirthDate.builder().day(2).month(1).year(1990).build())
                .address(
                        Address.builder()
                                .line1("Abbey Road 1")
                                .city("Brentford")
                                .region("London")
                                .country("UK")
                                .postalCode("12341")
                                .build())
                .build();
    }
}
