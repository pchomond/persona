package com.pchomond.persona.mapper;

import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.model.UserEntity.UserAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.model.Address;
import org.openapitools.model.BirthDate;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldMapCreateUserRequestToUserEntity_whenAllFieldsArePresent() {
        // given
        BirthDate birthDateDto = BirthDate.builder().year(1990).month(5).day(15).build();
        Address addressDto = Address.builder()
                .line1("123 Main St")
                .line2(JsonNullable.of("Apt 4B"))
                .city("Springfield")
                .postalCode("12345")
                .country("USA")
                .build();

        CreateUserRequest request = CreateUserRequest.builder()
                .givenName("John")
                .surname("Doe")
                .email("john.doe@gmail.com")
                .dateOfBirth(birthDateDto)
                .address(addressDto)
                .build();

        // when
        UserEntity result = userMapper.toUserEntity(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGivenName()).isEqualTo("John");
        assertThat(result.getSurname()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@gmail.com");

        assertThat(result.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 5, 15));

        assertThat(result.getAddress()).isNotNull();
        assertThat(result.getAddress().getLine1()).isEqualTo("123 Main St");
        assertThat(result.getAddress().getLine2()).isEqualTo("Apt 4B");
        assertThat(result.getAddress().getCity()).isEqualTo("Springfield");
        assertThat(result.getAddress().getPostalCode()).isEqualTo("12345");
        assertThat(result.getAddress().getCountry()).isEqualTo("USA");
    }

    @Test
    void shouldMapCreateUserRequestToUserEntity_whenNestedObjectsAreNull() {
        // given
        CreateUserRequest request = CreateUserRequest.builder()
                .givenName("Jane")
                .surname("Smith")
                .email("jane.smith@example.com")
                .dateOfBirth(null)
                .address(null)
                .build();

        // when
        UserEntity result = userMapper.toUserEntity(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGivenName()).isEqualTo("Jane");
        assertThat(result.getDateOfBirth()).isNull();
        assertThat(result.getAddress()).isNull();
    }

    @Test
    void shouldMapUserEntityToUser_whenAllFieldsArePresent() {
        // given
        UserAddress entityAddress = UserAddress.builder()
                .line1("456 Oak Ave")
                .line2("Suite 100")
                .city("Metropolis")
                .postalCode("67890")
                .country("USA")
                .build();

        UserEntity entity = UserEntity.builder()
                .givenName("Clark")
                .surname("Kent")
                .email("clark.kent@gmail.com")
                .dateOfBirth(LocalDate.of(1985, 10, 20))
                .address(entityAddress)
                .build();

        // when
        User result = userMapper.toUser(entity);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGivenName()).isEqualTo("Clark");
        assertThat(result.getSurname()).isEqualTo("Kent");
        assertThat(result.getEmail()).isEqualTo("clark.kent@gmail.com");

        assertThat(result.getDateOfBirth()).isNotNull();
        assertThat(result.getDateOfBirth().getYear()).isEqualTo(1985);
        assertThat(result.getDateOfBirth().getMonth()).isEqualTo(10);
        assertThat(result.getDateOfBirth().getDay()).isEqualTo(20);

        assertThat(result.getAddress()).isNotNull();
        assertThat(result.getAddress().getLine1()).isEqualTo("456 Oak Ave");
        assertThat(result.getAddress().getLine2().isPresent()).isTrue();
        assertThat(result.getAddress().getLine2().get()).isEqualTo("Suite 100");
        assertThat(result.getAddress().getCity()).isEqualTo("Metropolis");
        assertThat(result.getAddress().getPostalCode()).isEqualTo("67890");
        assertThat(result.getAddress().getCountry()).isEqualTo("USA");
    }

    @Test
    void shouldMapUserEntityToUser_whenNestedObjectsAreNull() {
        // given
        UserEntity entity = UserEntity.builder()
                .givenName("Bruce")
                .surname("Wayne")
                .email("bruce.wayne@gmail.com")
                .dateOfBirth(null)
                .address(null)
                .build();

        // when
        User result = userMapper.toUser(entity);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGivenName()).isEqualTo("Bruce");
        assertThat(result.getDateOfBirth()).isNull();
        assertThat(result.getAddress()).isNull();
    }
}
