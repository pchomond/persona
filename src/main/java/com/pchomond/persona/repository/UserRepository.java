package com.pchomond.persona.repository;

import com.pchomond.persona.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByIdpId(UUID idpId);

    boolean existsByEmail(String email);
}
