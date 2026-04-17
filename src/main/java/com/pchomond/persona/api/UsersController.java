package com.pchomond.persona.api;

import com.pchomond.persona.api.validation.UserRequestValidator;
import com.pchomond.persona.exception.RequestValidationException;
import com.pchomond.persona.service.UserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.UsersApi;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.ErrorDetail;
import org.openapitools.model.UpdateUserRequest;
import org.openapitools.model.User;
import org.openapitools.model.UserList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UserRequestValidator userRequestValidator;
    private final UserService userService;

    @Override
    public ResponseEntity<User> createUser(CreateUserRequest createUserRequest) {
        List<ErrorDetail> errors = userRequestValidator.validate(createUserRequest);

        if (!errors.isEmpty()) throw new RequestValidationException(errors);

        User createdUser = userService.createAndValidateUser(createUserRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteUser(String userId) {
        return null;
    }

    @Override
    public ResponseEntity<User> getUser(String userId) {
        return null;
    }

    @Override
    public ResponseEntity<UserList> getUserList() {
        return null;
    }

    @Override
    public ResponseEntity<User> updateUser(String userId, UpdateUserRequest updateUserRequest) {
        return null;
    }
}
