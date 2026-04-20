package com.pchomond.persona.api;

import com.pchomond.persona.api.validation.UserRequestValidator;
import com.pchomond.persona.exception.RequestValidationException;
import com.pchomond.persona.service.UserService;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Address;
import org.openapitools.model.BirthDate;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.ErrorDetail;
import org.openapitools.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@WebMvcTest(UsersController.class)
@AutoConfigureRestTestClient
public class UsersControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRequestValidator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void createUser_shouldReturnUser_WhenValidRequest() {
        // given
        var createUserRequest = createCreateUserRequest();
        var createdUser = createUser();

        given(userService.createAndValidateUser(createUserRequest)).willReturn(createdUser);

        // when & then
        restTestClient.post().uri("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(createUserRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody().json(objectMapper.writeValueAsString(createdUser));

        verify(validator).validate(createUserRequest);
        verify(userService).createAndValidateUser(createUserRequest);
    }

    @Test
    void createUser_shouldReturnBadRequestError_WhenInvalidRequest() {
        // given
        CreateUserRequest invalidCreateUserRequest = createInvalidCreateUserRequest();
        var createdUser = createUser();

        willThrow(new RequestValidationException(List.of())).given(validator).validate(invalidCreateUserRequest);
        given(userService.createAndValidateUser(invalidCreateUserRequest)).willReturn(createdUser);

        // when & then
        restTestClient.post().uri("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(invalidCreateUserRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.details").isArray();

        verify(validator).validate(invalidCreateUserRequest);
        verifyNoInteractions(userService);
    }

    private static CreateUserRequest createInvalidCreateUserRequest() {
        return CreateUserRequest.builder()
                .idpId(UUID.randomUUID().toString())
                .email("test@.com")
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

    private static CreateUserRequest createCreateUserRequest() {
        return CreateUserRequest.builder()
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

    private static User createUser() {
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
