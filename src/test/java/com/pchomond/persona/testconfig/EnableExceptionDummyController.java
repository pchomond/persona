package com.pchomond.persona.testconfig;

import com.pchomond.persona.exception.GlobalExceptionHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ ExceptionControllerTestConfig.class, GlobalExceptionHandler.class })
@EnableWebMvc
@WebMvcTest(controllers = ExceptionThrowingController.class)
public @interface EnableExceptionDummyController {

}
