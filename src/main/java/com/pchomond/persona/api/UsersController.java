package com.pchomond.persona.api;

import org.openapitools.api.UsersApi;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.UpdateUserRequest;
import org.openapitools.model.User;
import org.openapitools.model.UsersList;
import org.springframework.http.ResponseEntity;

public class UsersController implements UsersApi {

    @Override
    public ResponseEntity<User> createUser(CreateUserRequest createUserRequest) {
        return null;
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
    public ResponseEntity<UsersList> getUserList() {
        return null;
    }

    @Override
    public ResponseEntity<User> updateUser(String userId, UpdateUserRequest updateUserRequest) {
        return null;
    }
}
