package com.pchomond.persona.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    UUID userId;

    @Column(name = "email")
    String email;

    @Column(name = "surname")
    String surname;

    @Column(name = "given_name")
    String givenName;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Embedded
    UserAddress address;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @Embeddable
    public static class UserAddress {

        @Column(name = "city")
        String city;

        @Column(name = "address_line_1")
        String line1;

        @Column(name = "address_line_2")
        String line2;

        @Column(name = "region")
        String region;

        @Column(name = "country")
        String country;

        @Column(name = "postal_code")
        String postalCode;
    }
}
