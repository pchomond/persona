package com.pchomond.persona.mapper;

import com.pchomond.persona.model.UserEntity;
import com.pchomond.persona.model.UserEntity.UserAddress;
import org.openapitools.model.Address;
import org.openapitools.model.BirthDate;
import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserMapper {

    public UserEntity toUserEntity(CreateUserRequest createUserRequest) {
        return UserEntity.builder()
                .surname(createUserRequest.getSurname())
                .givenName(createUserRequest.getGivenName())
                .email(createUserRequest.getEmail())
                .dateOfBirth(toLocalDate(createUserRequest.getDateOfBirth()))
                .address(toUserAddress(createUserRequest.getAddress()))
                .build();
    }

    private static LocalDate toLocalDate(BirthDate dateDto) {
        return dateDto == null
                ? null
                : LocalDate.of(
                        dateDto.getYear(),
                        dateDto.getMonth(),
                        dateDto.getDay()
                );
    }

    private static UserAddress toUserAddress(Address addressDto) {
        return addressDto == null
                ? null
                : UserAddress.builder()
                    .line1(addressDto.getLine1())
                    .line2(addressDto.getLine2().get())
                    .city(addressDto.getCity())
                    .postalCode(addressDto.getPostalCode())
                    .country(addressDto.getCountry())
                    .build();
    }

    public User toUser(UserEntity userEntity) {
        return User.builder()
                .email(userEntity.getEmail())
                .givenName(userEntity.getGivenName())
                .surname(userEntity.getSurname())
                .dateOfBirth(toBirthDate(userEntity.getDateOfBirth()))
                .address(toAddress(userEntity.getAddress()))
                .build();

    }

    private static BirthDate toBirthDate(LocalDate localDate) {
        return BirthDate.builder()
                .year(localDate.getYear())
                .month(localDate.getMonthValue())
                .day(localDate.getDayOfMonth())
                .build();
    }

    private static Address toAddress(UserAddress userAddress) {
        return Address.builder()
                .line1(userAddress.getLine1())
                .line2(userAddress.getLine2())
                .city(userAddress.getCity())
                .postalCode(userAddress.getPostalCode())
                .country(userAddress.getCountry())
                .build();
    }
}
