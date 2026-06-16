package com.pchomond.persona.service.validation;

import com.pchomond.persona.exception.ResourceConflictException;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.pchomond.persona.exception.domain.UserConflictErrorCode.EMAIL_ALREADY_EXISTS;
import static com.pchomond.persona.exception.domain.UserConflictErrorCode.IDP_ID_ALREADY_EXISTS;

@Component
@AllArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validate(UserEntity entity) {

        if (entity.getIdpId() != null && userRepository.existsByIdpId(entity.getIdpId())) {
            throw new ResourceConflictException(IDP_ID_ALREADY_EXISTS, entity.getIdpId().toString());
        }

        if (userRepository.existsByEmail(entity.getEmail())) {
            throw new ResourceConflictException(EMAIL_ALREADY_EXISTS, entity.getEmail());
        }
    }
}
