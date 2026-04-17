package com.pchomond.persona.service;

import com.pchomond.persona.exception.BusinessValidationException;
import com.pchomond.persona.mapper.UserMapper;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.repository.UserRepository;
import com.pchomond.persona.service.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.ErrorDetail;
import org.openapitools.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final UserRepository userRepository;

    public User createAndValidateUser(CreateUserRequest createUserRequest) {
        var mappedUser = userMapper.toUserEntity(createUserRequest);

        List<ErrorDetail> errors = userValidator.validate(mappedUser);

        if (!errors.isEmpty()) throw new BusinessValidationException(errors);

        UserEntity persistedUser = userRepository.save(mappedUser);
        return userMapper.toUser(persistedUser);
    }
}
