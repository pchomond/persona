package com.pchomond.persona.service;

import com.pchomond.persona.mapper.UserMapper;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void givenCreateUserRequest_shouldCreateAndValidateUser() {
        // given
        var createUserRequest = mock(CreateUserRequest.class);
        var mockedUserEntity = mock(UserEntity.class);
        given(userMapper.toUserEntity(eq(createUserRequest))).willReturn(mockedUserEntity);
        given(userRepository.save(mockedUserEntity)).willReturn(mockedUserEntity);
        given(userMapper.toUser(mockedUserEntity)).willReturn(mock(User.class));

        // when
        var result = userService.createAndValidateUser(createUserRequest);

        // then
        assertThat(result).isExactlyInstanceOf(User.class);
    }
}
