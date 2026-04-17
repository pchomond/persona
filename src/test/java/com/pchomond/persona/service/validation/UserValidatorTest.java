package com.pchomond.persona.service.validation;

import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ErrorDetail;

import java.time.LocalDate;
import java.util.UUID;

import static com.pchomond.persona.validation.UserValidationConstants.FIELD_EMAIL;
import static com.pchomond.persona.validation.UserValidationConstants.FIELD_IDP_ID;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_EMAIL_EXISTS;
import static com.pchomond.persona.validation.UserValidationConstants.MSG_IDP_ID_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void validate_shouldReturnEmptyList_whenNonErrorsOccur() {
        // given
        var userEntity = generateUserEntity();

        given(userRepository.existsByIdpId(eq(userEntity.getIdpId()))).willReturn(false);
        given(userRepository.existsByEmail(eq(userEntity.getEmail()))).willReturn(false);

        // when
        var errors = userValidator.validate(userEntity);

        // then
        assertThat(errors).isEmpty();

        verify(userRepository).existsByIdpId(userEntity.getIdpId());
        verify(userRepository).existsByEmail(userEntity.getEmail());
    }

    @Test
    void validate_shouldReturnError_whenIdpIdAlreadyExists() {
        // given
        var userEntity = generateUserEntity();

        given(userRepository.existsByIdpId(eq(userEntity.getIdpId()))).willReturn(true);
        given(userRepository.existsByEmail(eq(userEntity.getEmail()))).willReturn(false);

        // when
        var errors = userValidator.validate(userEntity);

        // then
        assertThat(errors)
                .hasSize(1)
                .extracting(ErrorDetail::getField, ErrorDetail::getReason)
                .containsExactly(tuple(FIELD_IDP_ID, MSG_IDP_ID_EXISTS));

        verify(userRepository).existsByIdpId(userEntity.getIdpId());
        verify(userRepository).existsByEmail(userEntity.getEmail());
    }

    @Test
    void validate_shouldReturnError_whenEmailAlreadyExists() {
        // given
        var userEntity = generateUserEntity();

        given(userRepository.existsByIdpId(eq(userEntity.getIdpId()))).willReturn(false);
        given(userRepository.existsByEmail(eq(userEntity.getEmail()))).willReturn(true);

        // when
        var errors = userValidator.validate(userEntity);

        // then
        assertThat(errors)
                .hasSize(1)
                .extracting(ErrorDetail::getField, ErrorDetail::getReason)
                .containsExactly(tuple(FIELD_EMAIL, MSG_EMAIL_EXISTS));

        verify(userRepository).existsByIdpId(userEntity.getIdpId());
        verify(userRepository).existsByEmail(userEntity.getEmail());
    }

    private static UserEntity generateUserEntity() {
        return UserEntity.builder()
                .userId(UUID.randomUUID())
                .idpId(UUID.randomUUID())
                .email("test@gmail.com")
                .givenName("Test")
                .surname("User")
                .dateOfBirth(LocalDate.of(1990, 1, 2))
                .address(UserEntity.UserAddress.builder()
                        .line1("Abbey Road 1")
                        .city("Brentford")
                        .region("London")
                        .country("UK")
                        .postalCode("12341")
                        .build())
                .build();
    }
}
