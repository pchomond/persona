package com.pchomond.persona.repository;

import com.pchomond.persona.testconfig.EnablePostgresTestContainer;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.model.UserEntity.UserAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@EnablePostgresTestContainer
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        // Given
        user = new UserEntity();
        user.setUserId(UUID.randomUUID());
        user.setEmail("foo@bar.com");
        user.setSurname("Test");
        user.setGivenName("User");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setAddress(UserAddress.builder()
                .city("City")
                .line1("Address1")
                .region("Region")
                .postalCode("12345")
                .build());

        // When
        userRepository.save(user);
    }

    @Test
    void givenUser_WhenSave_ThenFindByUserIdTest() {
        // Then
        UserEntity retrievedUser = userRepository.findByUserId(user.getUserId());
        assertEquals(retrievedUser, user);
    }
}
