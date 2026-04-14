package com.pchomond.persona.config;

import org.junit.jupiter.api.Test;
import org.openapitools.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class JacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDeserialize() {
        String json = "{\"surname\":\"   Papadopoulos  \"}";
        User user = objectMapper.readValue(json, User.class);
        assertEquals("Papadopoulos", user.getSurname());
    }
}
