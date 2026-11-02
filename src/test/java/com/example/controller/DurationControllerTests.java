package com.example.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DurationController.class)
@DisplayName("DurationController Tests")
class DurationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("GET /duration/get")
    class GetDurationTests {

        @Test
        @DisplayName("should return 1 second duration in ISO-8601 format (PT1S)")
        void shouldReturnPredefinedDuration() throws Exception {
            mockMvc.perform(get("/duration/get"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value("PT1S"));
        }
    }

    @Nested
    @DisplayName("GET /duration")
    class DurationParamTests {

        @ParameterizedTest(name = "should parse {0} and return canonical duration {1}")
        @CsvSource({
                "PT1S, PT1S",
                "PT15M, PT15M",
                "PT2H, PT2H",
                "P1D, PT24H"
        })
        @DisplayName("should parse valid ISO-8601 duration query parameter into canonical duration")
        void shouldParseAndReturnValidDuration(String inputDuration, String expectedDuration) throws Exception {
            mockMvc.perform(get("/duration").param("duration", inputDuration))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value(expectedDuration));
        }

        @Test
        @DisplayName("should return 400 Bad Request for invalid duration string")
        void shouldReturnBadRequestForInvalidDuration() throws Exception {
            mockMvc.perform(get("/duration").param("duration", "invalid-duration"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.title").value("Bad Request"));
        }

        @Test
        @DisplayName("should return 400 Bad Request when duration parameter is missing")
        void shouldReturnBadRequestWhenDurationMissing() throws Exception {
            mockMvc.perform(get("/duration"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400));
        }
    }
}
