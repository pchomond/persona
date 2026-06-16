package com.pchomond.persona.api.validation;

import com.pchomond.persona.exception.RequestValidationException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Address;
import org.openapitools.model.BirthDate;
import org.openapitools.model.CreateUserRequest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import org.openapitools.model.FieldViolation;

import static com.pchomond.persona.exception.domain.GlobalErrorCode.VALIDATION_ERROR;
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

@ExtendWith(MockitoExtension.class)
public class UserRequestValidatorTest {

    private UserRequestValidator userRequestValidator;

    @BeforeEach
    public void init() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-04-17T10:00:00Z"), ZoneOffset.UTC);
        userRequestValidator = new UserRequestValidator(fixedClock);
    }

    @Nested
    @DisplayName("Happy path scenarios")
    class NoValidationErrors {

        @Test
        @DisplayName("Should validate successfully when a valid email is valid and birth date is absent")
        void shouldPassWhenEmailIsValidAndBirthDateIsAbsent() {
            // given
            CreateUserRequest request = generateCreateUserRequest();
            request.setEmail("valid@email.com");
            request.setDateOfBirth(null);

            // when & then
            assertThatNoException().isThrownBy(() -> userRequestValidator.validate(request));
        }

        @Test
        @DisplayName("Should validate successfully when a valid email is valid and birth date is valid")
        void shouldPassWhenEmailIsValidAndBirthDateIsValid() {
            // given
            CreateUserRequest request = generateCreateUserRequest();
            request.setEmail("valid@email.com");
            request.setDateOfBirth(BirthDate.builder().day(1).month(2).year(1990).build());

            // when & then
            assertThatNoException().isThrownBy(() -> userRequestValidator.validate(request));
        }
    }

    @Nested
    @DisplayName("Email validation tests")
    class EmailValidationTests {

        @DisplayName("Should throw exception when email is invalid")
        @ParameterizedTest(name = "For example, email input {0} is not supported.")
        @CsvSource({
                "plainaddress",
                "@missingusername.com",
                "username@missingdomain",
                "username@.com"
        })
        void shouldThrowExceptionWhenEmailIsInvalid(String invalidEmail) {
            // given
            CreateUserRequest request = generateCreateUserRequest();
            request.setEmail(invalidEmail);

            // when & then
            assertThatThrownBy(() -> userRequestValidator.validate(request))
                    .isInstanceOf(RequestValidationException.class)
                    .satisfies(ex -> {
                        RequestValidationException requestValidationException = (RequestValidationException) ex;
                        assertThat(requestValidationException.getErrorCode()).isEqualTo(VALIDATION_ERROR);

                        List<FieldViolation> errors = requestValidationException.getErrors();
                        assertThat(errors).hasSize(1);
                        assertThat(errors).extracting(FieldViolation::getField, FieldViolation::getMessage)
                                .containsExactly(
                                        tuple(FIELD_EMAIL, MSG_INVALID_EMAIL_FORMAT)
                                );
                    });
        }
    }

    @Nested
    @DisplayName("Birthdate validation tests")
    class BirthDateValidationTests {

        @Test
        @DisplayName("Should throw exception when birth date fields cross lower boundaries")
        void shouldThrowExceptionWhenBirthDateFieldsAreBelowMinimumBoundaries() {
            // given
            CreateUserRequest request = generateCreateUserRequest();
            var invalidDob = BirthDate.builder().year(1919).month(0).day(0).build();
            request.setDateOfBirth(invalidDob);

            // when & then
            assertThatThrownBy(() -> userRequestValidator.validate(request))
                    .isInstanceOf(RequestValidationException.class)
                    .satisfies(ex -> {
                        RequestValidationException requestValidationException = (RequestValidationException) ex;
                        assertThat(requestValidationException.getErrorCode()).isEqualTo(VALIDATION_ERROR);


                        List<FieldViolation> errors = requestValidationException.getErrors();
                        assertThat(errors).hasSize(3);
                        assertThat(errors).extracting(FieldViolation::getField, FieldViolation::getMessage)
                                .containsExactlyInAnyOrder(
                                        tuple(FIELD_DOB_YEAR, MSG_INVALID_YEAR),
                                        tuple(FIELD_DOB_MONTH, MSG_INVALID_MONTH),
                                        tuple(FIELD_DOB_DAY, MSG_INVALID_DAY)
                                );
                    });
        }

        @Test
        @DisplayName("Should throw exception when birth date fields cross upper boundaries")
        void shouldThrowExceptionWhenBirthDateFieldsAreAboveMaximumBoundaries() {
            // given
            CreateUserRequest request = generateCreateUserRequest();
            var invalidDob = BirthDate.builder().year(2027).month(13).day(32).build();
            request.setDateOfBirth(invalidDob);

            // when & then
            assertThatThrownBy(() -> userRequestValidator.validate(request))
                    .isInstanceOf(RequestValidationException.class)
                    .satisfies(ex -> {
                        RequestValidationException requestValidationException = (RequestValidationException) ex;
                        assertThat(requestValidationException.getErrorCode()).isEqualTo(VALIDATION_ERROR);

                        List<FieldViolation> errors = requestValidationException.getErrors();
                        assertThat(errors).hasSize(3);
                        assertThat(errors).extracting(FieldViolation::getField, FieldViolation::getMessage)
                                .containsExactlyInAnyOrder(
                                        tuple(FIELD_DOB_YEAR, MSG_INVALID_YEAR),
                                        tuple(FIELD_DOB_MONTH, MSG_INVALID_MONTH),
                                        tuple(FIELD_DOB_DAY, MSG_INVALID_DAY)
                                );
                    });
        }

        @Test
        @DisplayName("Should throw exception when birth date is logically invalid")
        void shouldThrowExceptionWhenBirthDateIsLogicallyInvalid() {
            // given
            CreateUserRequest request = generateCreateUserRequest();
            var invalidDob = BirthDate.builder().year(2022).month(2).day(30).build();
            request.setDateOfBirth(invalidDob);

            // when & then
            assertThatThrownBy(() -> userRequestValidator.validate(request))
                    .isInstanceOf(RequestValidationException.class)
                    .satisfies(ex -> {
                        RequestValidationException requestValidationException = (RequestValidationException) ex;
                        assertThat(requestValidationException.getErrorCode()).isEqualTo(VALIDATION_ERROR);

                        List<FieldViolation> errors = requestValidationException.getErrors();
                        assertThat(errors).hasSize(1);
                        assertThat(errors).extracting(FieldViolation::getField, FieldViolation::getMessage)
                                .containsExactly(
                                        tuple(FIELD_DOB, MSG_INVALID_DOB)
                                );
                    });
        }
    }

    @Nested
    @DisplayName("Aggregate validation tests")
    class AggregateValidationTests {

        @Test
        @DisplayName("Should throw exception containing all accumulated validation errors")
        void shouldThrowExceptionWhenMultipleFieldsAreInvalid() {
            CreateUserRequest request = generateCreateUserRequest();
            var invalidDob = BirthDate.builder().year(2027).month(13).day(32).build();
            request.setDateOfBirth(invalidDob);
            request.setEmail("bad-email");

            // when & then
            assertThatThrownBy(() -> userRequestValidator.validate(request))
                    .isInstanceOf(RequestValidationException.class)
                    .satisfies(ex -> {
                        RequestValidationException requestValidationException = (RequestValidationException) ex;
                        assertThat(requestValidationException.getErrorCode()).isEqualTo(VALIDATION_ERROR);

                        List<FieldViolation> errors = requestValidationException.getErrors();
                        assertThat(errors).hasSize(4);
                        assertThat(errors).extracting(FieldViolation::getField, FieldViolation::getMessage)
                                .containsExactlyInAnyOrder(
                                        tuple(FIELD_DOB_YEAR, MSG_INVALID_YEAR),
                                        tuple(FIELD_DOB_MONTH, MSG_INVALID_MONTH),
                                        tuple(FIELD_DOB_DAY, MSG_INVALID_DAY),
                                        tuple(FIELD_EMAIL, MSG_INVALID_EMAIL_FORMAT)
                                );
                    });
        }
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
