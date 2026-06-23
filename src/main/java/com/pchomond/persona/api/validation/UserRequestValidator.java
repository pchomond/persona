package com.pchomond.persona.api.validation;

import com.pchomond.persona.exception.RequestValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import com.pchomond.persona.api.rest.model.BirthDate;
import com.pchomond.persona.api.rest.model.CreateUserRequest;
import com.pchomond.persona.api.rest.model.FieldViolation;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

@Component
@RequiredArgsConstructor
public class UserRequestValidator {

    private static final int MIN_BIRTH_YEAR = 1920;

    private final Clock systemClock;

    public void validate(CreateUserRequest request) {
        List<FieldViolation> errors = new ArrayList<>();

        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
            errors.add(new FieldViolation(FIELD_EMAIL, MSG_INVALID_EMAIL_FORMAT));
        }

        BirthDate birthDate = request.getDateOfBirth();
        if (birthDate != null) {
            validateBirthDate(birthDate, errors);
        }

        if (!errors.isEmpty()) throw new RequestValidationException(errors);
    }

    private void validateBirthDate(BirthDate birthDate, List<FieldViolation> errors) {
        if (birthDate == null) return;

        LocalDate today = LocalDate.now(systemClock);

        Integer d = birthDate.getDay();
        Integer m = birthDate.getMonth();
        Integer y = birthDate.getYear();

        if (y < MIN_BIRTH_YEAR || y > today.getYear()) errors.add(new FieldViolation(FIELD_DOB_YEAR, MSG_INVALID_YEAR));
        if (m < 1 || m > 12) errors.add(new FieldViolation(FIELD_DOB_MONTH, MSG_INVALID_MONTH));
        if (d < 1 || d > 31) errors.add(new FieldViolation(FIELD_DOB_DAY, MSG_INVALID_DAY));

        if (!errors.isEmpty()) return;

        try {
            LocalDate.of(y, m, d);
        } catch (DateTimeException e) {
            errors.add(new FieldViolation(FIELD_DOB, MSG_INVALID_DOB));
        }
    }
}
