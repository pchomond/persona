package com.pchomond.persona.repository;

import com.pchomond.persona.testconfig.EnablePostgresTestContainer;
import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.model.UserEntity.UserAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@EnablePostgresTestContainer
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenUser_WhenSave_ThenExistsByIdpIdTest() {
        // given
        UserEntity userEntity = generateUserEntity();
        userRepository.save(userEntity);

        // when
        boolean isUser = userRepository.existsByIdpId(userEntity.getIdpId());

        // then
        assertTrue(isUser);
    }

    @Test
    void givenUser_WhenSave_ThenExistsByEmailTest() {
        // given
        UserEntity userEntity = generateUserEntity();
        userRepository.save(userEntity);

        // when
        boolean isUser = userRepository.existsByEmail(userEntity.getEmail());

        // then
        assertTrue(isUser);
    }

    private UserEntity generateUserEntity() {
        return UserEntity.builder()
                .userId(UUID.randomUUID())
                .idpId(UUID.randomUUID())
                .email("test@gmail.com")
                .givenName("Test")
                .surname("User")
                .dateOfBirth(LocalDate.of(1990, 1, 2))
                .address(UserAddress.builder()
                        .line1("Abbey Road 1")
                        .city("Brentford")
                        .region("London")
                        .country("UK")
                        .postalCode("12341")
                        .build())
                .build();
    }
}
