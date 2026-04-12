package com.pchomond.persona.service;

import com.pchomond.persona.mapper.UserMapper;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createAndValidateUser(CreateUserRequest createUserRequest) {
        UserEntity user = UserMapper.toUserEntity(createUserRequest);
        var persistedUser = userRepository.save(user);
        return UserMapper.toUser(persistedUser);
    }
}
