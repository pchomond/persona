package com.pchomond.persona.api;

import com.pchomond.persona.service.UserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.UsersApi;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.UpdateUserRequest;
import org.openapitools.model.User;
import org.openapitools.model.UserList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<User> createUser(CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createAndValidateUser(createUserRequest));
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
