package com.pchomond.persona.service;

import com.pchomond.persona.mapper.UserMapper;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.model.UserEntity.UserAddress;
import com.pchomond.persona.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Address;
import org.openapitools.model.BirthDate;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.User;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createAndValidateUser_shouldReturnUser_whenNoErrorsOccur() {
        // given
        var createUserRequest = generateCreateUserRequest();
        var mappedUserEntity = generateUserEntity();
        var persistedUserEntity = generateUserEntity();
        var expectedUser = generateUser();

        given(userMapper.toUserEntity(eq(createUserRequest))).willReturn(mappedUserEntity);
        given(userRepository.save(mappedUserEntity)).willReturn(persistedUserEntity);
        given(userMapper.toUser(persistedUserEntity)).willReturn(expectedUser);

        // when
        var result = userService.createAndValidateUser(createUserRequest);

        // then
        assertThat(result).isNotNull().isExactlyInstanceOf(User.class);

        verify(userMapper).toUserEntity(createUserRequest);
        verify(userRepository).save(mappedUserEntity);
        verify(userMapper).toUser(persistedUserEntity);

        verifyNoMoreInteractions(userMapper, userRepository);
    }

    @Test
    void createAndValidateUser_shouldThrowException_whenRepositoryFails() {
        // given
        CreateUserRequest createUserRequest = generateCreateUserRequest();
        UserEntity mappedUserEntity = generateUserEntity();

        when(userMapper.toUserEntity(createUserRequest)).thenReturn(mappedUserEntity);

        RuntimeException dbException = new RuntimeException("Database down");
        when(userRepository.save(any(UserEntity.class))).thenThrow(dbException);

        // when & then
        assertThatThrownBy(() -> userService.createAndValidateUser(createUserRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database down");

        verify(userMapper).toUserEntity(createUserRequest);
        verify(userRepository).save(mappedUserEntity);

        verifyNoMoreInteractions(userMapper, userRepository);
    }

    private static CreateUserRequest generateCreateUserRequest() {
        return CreateUserRequest.builder()
                .email("test@gmail.com")
                .givenName("Test")
                .surname("User")
                .dateOfBirth(BirthDate.builder()
                        .day(2)
                        .month(1)
                        .year(1990)
                        .build())
                .address(Address.builder()
                        .line1("Abbey Road 1")
                        .city("Brentford")
                        .region("London")
                        .country("UK")
                        .postalCode("12341")
                        .build())
                .build();
    }

    private static UserEntity generateUserEntity() {
        return UserEntity.builder()
                .userId(UUID.randomUUID())
                .idpId(UUID.randomUUID())
                .email("test@gmail.com")
                .givenName("Test")
                .surname("User")
                .dateOfBirth(LocalDate.of(1990, 1, 2))
                .address(UserAddress.builder()
                        .line1("Abbey Road 1")
                        .city("Brentford")
                        .region("London")
                        .country("UK")
                        .postalCode("12341")
                        .build())
                .build();
    }

    private static User generateUser() {
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .idpId(UUID.randomUUID().toString())
                .email("test@gmail.com")
                .givenName("Test")
                .surname("User")
                .dateOfBirth(BirthDate.builder()
                        .day(2)
                        .month(1)
                        .year(1990)
                        .build())
                .address(Address.builder()
                        .line1("Abbey Road 1")
                        .city("Brentford")
                        .region("London")
                        .country("UK")
                        .postalCode("12341")
                        .build())
                .build();
    }
}
