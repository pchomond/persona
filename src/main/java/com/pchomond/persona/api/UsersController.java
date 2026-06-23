package com.pchomond.persona.api;

import com.pchomond.persona.api.rest.controller.UsersApi;
import com.pchomond.persona.api.validation.UserRequestValidator;
import com.pchomond.persona.service.UserService;
import lombok.RequiredArgsConstructor;
import com.pchomond.persona.api.rest.model.CreateUserRequest;
import com.pchomond.persona.api.rest.model.UpdateUserRequest;
import com.pchomond.persona.api.rest.model.User;
import com.pchomond.persona.api.rest.model.UserList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UserRequestValidator userRequestValidator;
    private final UserService userService;

    @Override
    public ResponseEntity<User> createUser(CreateUserRequest createUserRequest) {
        userRequestValidator.validate(createUserRequest);

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
