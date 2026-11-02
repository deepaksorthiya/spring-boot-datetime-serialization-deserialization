package com.example.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InstantController.class)
@DisplayName("InstantController Tests")
class InstantControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("GET /instant/get")
    class GetInstantTests {

        @Test
        @DisplayName("should return current Instant in ISO-8601 UTC format")
        void shouldReturnCurrentInstant() throws Exception {
            String response = mockMvc.perform(get("/instant/get"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?Z$")))
                    .andReturn().getResponse().getContentAsString();

            // Strip enclosing quotes from JSON string
            String rawInstant = response.replace("\"", "");
            Instant parsedInstant = Instant.parse(rawInstant);
            assertThat(parsedInstant).isNotNull();
        }
    }

    @Nested
    @DisplayName("GET /instant")
    class InstantParamTests {

        @ParameterizedTest(name = "should parse and return instant for input: {0}")
        @ValueSource(strings = {"2025-06-15T10:30:00Z", "2025-12-31T23:59:59.999Z"})
        @DisplayName("should parse valid ISO-8601 Instant query parameter")
        void shouldParseAndReturnValidInstant(String instantParam) throws Exception {
            mockMvc.perform(get("/instant").param("instant", instantParam))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value(instantParam));
        }

        @Test
        @DisplayName("should return 400 Bad Request for invalid instant string")
        void shouldReturnBadRequestForInvalidInstant() throws Exception {
            mockMvc.perform(get("/instant").param("instant", "invalid-instant"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.title").value("Bad Request"));
        }

        @Test
        @DisplayName("should return 400 Bad Request when instant parameter is missing")
        void shouldReturnBadRequestWhenInstantMissing() throws Exception {
            mockMvc.perform(get("/instant"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400));
        }
    }
}
