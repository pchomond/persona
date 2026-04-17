package com.pchomond.persona.service.validation;

import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.openapitools.model.ErrorDetail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.pchomond.persona.validation.UserValidationConstants.FIELD_EMAIL;
import static com.pchomond.persona.validation.UserValidationConstants.FIELD_IDP_ID;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_EMAIL_EXISTS;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_IDP_ID_EXISTS;

@Component
@AllArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public List<ErrorDetail> validate(UserEntity entity) {
        List<ErrorDetail> errors = new ArrayList<>();

        if (entity.getIdpId() != null && userRepository.existsByIdpId(entity.getIdpId())) {
            errors.add(new ErrorDetail(FIELD_IDP_ID, MSG_IDP_ID_EXISTS));
        }

        if (userRepository.existsByEmail(entity.getEmail())) {
            errors.add(new ErrorDetail(FIELD_EMAIL, MSG_EMAIL_EXISTS));
        }

        return errors;
    }
}
