package com.pchomond.persona.testconfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ExceptionControllerTestConfig {

    @SuppressWarnings("ClassEscapesDefinedScope")
    @Bean
    public ExceptionThrowingController exceptionThrowingController() {
        return new ExceptionThrowingController();
    }
}
