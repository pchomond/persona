package com.pchomond.persona.service.validation;

import com.pchomond.persona.exception.ResourceConflictException;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static com.pchomond.persona.exception.domain.UserConflictErrorCode.EMAIL_ALREADY_EXISTS;
import static com.pchomond.persona.exception.domain.UserConflictErrorCode.IDP_ID_ALREADY_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Nested
    @DisplayName("Happy path scenarios")
    class HappyPathScenarios {
        @Test
        @DisplayName("Should pass when IdP Id and email are unique")
        void shouldPassWhenIdpIdAndEmailAreUnique() {
            // given
            var userEntity = generateUserEntity();

            given(userRepository.existsByIdpId(eq(userEntity.getIdpId()))).willReturn(false);
            given(userRepository.existsByEmail(eq(userEntity.getEmail()))).willReturn(false);

            // when & then
            assertThatNoException().isThrownBy(() -> userValidator.validate(userEntity));

            verify(userRepository).existsByIdpId(userEntity.getIdpId());
            verify(userRepository).existsByEmail(userEntity.getEmail());
        }

        @Test
        @DisplayName("Should pass when IdP Id is null and email is unique")
        void shouldPassWhenIdpIdIsNullAndEmailIsUnique() {
            // given
            var userEntity = generateUserEntity();
            userEntity.setIdpId(null);

            given(userRepository.existsByEmail(eq(userEntity.getEmail()))).willReturn(false);

            // when & then
            assertThatNoException().isThrownBy(() -> userValidator.validate(userEntity));

            verify(userRepository, times(0)).existsByIdpId(userEntity.getIdpId());
            verify(userRepository).existsByEmail(userEntity.getEmail());
        }
    }

    @Nested
    @DisplayName("Negative path scenarios")
    class NegativePathScenarios {

        @Test
        @DisplayName("Should throw exception when IdP Id already exists")
        void shouldThrowExceptionWhenIdpIdAlreadyExists() {
            // given
            var userEntity = generateUserEntity();

            given(userRepository.existsByIdpId(eq(userEntity.getIdpId()))).willReturn(true);

            // expect
            assertThatThrownBy(() -> userValidator.validate(userEntity))
                    .isInstanceOf(ResourceConflictException.class)
                    .satisfies(ex -> {
                        ResourceConflictException resourceConflictException = (ResourceConflictException) ex;
                        assertThat(resourceConflictException.getErrorCode()).isEqualTo(IDP_ID_ALREADY_EXISTS);
                        assertThat(resourceConflictException.getConflictingResource()).isEqualTo(IDP_ID_ALREADY_EXISTS.getConflictingResource());
                        assertThat(resourceConflictException.getConflictingField()).isEqualTo(IDP_ID_ALREADY_EXISTS.getConflictingField());
                        assertThat(resourceConflictException.getConflictingValue()).isEqualTo(userEntity.getIdpId().toString());
                    });

            verify(userRepository).existsByIdpId(userEntity.getIdpId());
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // given
            var userEntity = generateUserEntity();

            given(userRepository.existsByEmail(eq(userEntity.getEmail()))).willReturn(true);

            // expect
            assertThatThrownBy(() -> userValidator.validate(userEntity))
                    .isInstanceOf(ResourceConflictException.class)
                    .satisfies(ex -> {
                        ResourceConflictException resourceConflictException = (ResourceConflictException) ex;
                        assertThat(resourceConflictException.getErrorCode()).isEqualTo(EMAIL_ALREADY_EXISTS);
                        assertThat(resourceConflictException.getConflictingResource()).isEqualTo(EMAIL_ALREADY_EXISTS.getConflictingResource());
                        assertThat(resourceConflictException.getConflictingField()).isEqualTo(EMAIL_ALREADY_EXISTS.getConflictingField());
                        assertThat(resourceConflictException.getConflictingValue()).isEqualTo(userEntity.getEmail());
                    });

            verify(userRepository).existsByEmail(userEntity.getEmail());
        }
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
