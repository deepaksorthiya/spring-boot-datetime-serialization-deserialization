package com.example.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DateController.class)
class DateControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getDateDto_shouldReturnCurrentDate() throws Exception {
        String response = mockMvc.perform(get("/date"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        String date = json.get("localDate").asText();
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertThat(date).isEqualTo(today);
    }

    @Test
    void getDateDtoParam_shouldReturnGivenDate() throws Exception {
        String testDate = "2025-01-01";
        String response = mockMvc.perform(get("/date/withParam").param("date", testDate))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        assertThat(json.get("localDate").asText()).isEqualTo(testDate);
    }

    @Test
    void getDateDtoParam_withInvalidDate_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/date/withParam").param("date", "invalid-date"))
                .andExpect(status().isBadRequest());
    }
}