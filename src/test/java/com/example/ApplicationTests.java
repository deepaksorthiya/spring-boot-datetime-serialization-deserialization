package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("Application Context Tests")
class ApplicationTests {

    @Test
    @DisplayName("should load the application context without errors")
    void contextLoads() {
    }

    @Test
    @DisplayName("main method should start application successfully")
    void mainMethodStartsApplication() {
        assertDoesNotThrow(() -> Application.main(new String[]{}));
    }
}
