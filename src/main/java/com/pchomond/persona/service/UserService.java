package com.pchomond.persona.service;

import com.pchomond.persona.mapper.UserMapper;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.repository.UserRepository;
import com.pchomond.persona.service.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import com.pchomond.persona.api.rest.model.CreateUserRequest;
import com.pchomond.persona.api.rest.model.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final UserRepository userRepository;

    public User createAndValidateUser(CreateUserRequest createUserRequest) {
        var mappedUser = userMapper.toUserEntity(createUserRequest);

        userValidator.validate(mappedUser);

        UserEntity persistedUser = userRepository.save(mappedUser);
        return userMapper.toUser(persistedUser);
    }
}
