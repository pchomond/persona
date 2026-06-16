package com.pchomond.persona.api;

import com.pchomond.persona.api.validation.UserRequestValidator;
import com.pchomond.persona.exception.RequestValidationException;
import com.pchomond.persona.exception.ResourceConflictException;
import com.pchomond.persona.service.UserService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Address;
import org.openapitools.model.BirthDate;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static com.pchomond.persona.exception.domain.UserConflictErrorCode.EMAIL_ALREADY_EXISTS;
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
    private UserRequestValidator userRequestValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTestClient restTestClient;

    @Test
    @DisplayName("Should return a 201 response when a valid create request is submitted")
    void shouldReturnUserWhenValidRequest() {
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

        verify(userRequestValidator).validate(createUserRequest);
        verify(userService).createAndValidateUser(createUserRequest);
    }

    @Test
    @DisplayName("Should return a 400 response when an invalid request in submitted")
    void shouldReturnBadRequestErrorWhenInvalidRequest() {
        // given
        CreateUserRequest invalidCreateUserRequest = createInvalidCreateUserRequest();

        willThrow(new RequestValidationException(List.of())).given(userRequestValidator).validate(invalidCreateUserRequest);

        // when & then
        restTestClient.post().uri("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(invalidCreateUserRequest))
                .exchange()
                .expectStatus().isBadRequest();

        verify(userRequestValidator).validate(invalidCreateUserRequest);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Should return a 409 response when a resource conflict exception occurs")
    void createUser_shouldReturnBadRequestError_WhenResourceConflictRequest() {
        // given
        CreateUserRequest invalidCreateUserRequest = createInvalidCreateUserRequest();

        given(userService.createAndValidateUser(invalidCreateUserRequest))
                .willThrow(new ResourceConflictException(EMAIL_ALREADY_EXISTS, "clash@domain.com"));

        // when & then
        restTestClient.post().uri("/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(invalidCreateUserRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT.value());

        verify(userRequestValidator).validate(invalidCreateUserRequest);
        verify(userService).createAndValidateUser(invalidCreateUserRequest);
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
