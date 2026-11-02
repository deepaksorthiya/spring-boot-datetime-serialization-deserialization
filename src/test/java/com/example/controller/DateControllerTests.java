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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DateController.class)
@DisplayName("DateController Tests")
class DateControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("GET /date")
    class GetDateDtoTests {

        @Test
        @DisplayName("should return current date in ISO format")
        void getDateDto_shouldReturnCurrentDate() throws Exception {
            String response = mockMvc.perform(get("/date"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.localDate").isString())
                    .andExpect(jsonPath("$.localDate", matchesPattern("^\\d{4}-\\d{2}-\\d{2}$")))
                    .andReturn().getResponse().getContentAsString();

            // Extract date value and ensure it corresponds to today (or adjacent day around midnight)
            LocalDate today = LocalDate.now();
            assertThat(response).satisfiesAnyOf(
                    res -> assertThat(res).contains(today.toString()),
                    res -> assertThat(res).contains(today.minusDays(1).toString())
            );
        }
    }

    @Nested
    @DisplayName("GET /date/withParam")
    class GetDateDtoParamTests {

        @ParameterizedTest(name = "should parse and return date for: {0}")
        @ValueSource(strings = {"2025-01-01", "2025-12-31", "2026-02-28"})
        @DisplayName("should accept valid ISO LocalDate query parameter")
        void getDateDtoParam_shouldReturnGivenDate(String testDate) throws Exception {
            mockMvc.perform(get("/date/withParam").param("date", testDate))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.localDate").value(testDate));
        }

        @ParameterizedTest(name = "should return 400 Bad Request for invalid date: {0}")
        @ValueSource(strings = {"invalid-date", "2025-02-30", "01-01-2025"})
        @DisplayName("should return 400 Bad Request for invalid date formats")
        void getDateDtoParam_withInvalidDate_shouldReturnBadRequest(String invalidDate) throws Exception {
            mockMvc.perform(get("/date/withParam").param("date", invalidDate))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.title").value("Bad Request"));
        }

        @Test
        @DisplayName("should return 400 Bad Request when required date parameter is missing")
        void getDateDtoParam_whenParamMissing_shouldReturnBadRequest() throws Exception {
            mockMvc.perform(get("/date/withParam"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400));
        }
    }
}