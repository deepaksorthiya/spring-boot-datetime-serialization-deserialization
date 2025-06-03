package com.example.controller;

import com.example.model.TimeDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = TimeController.class)
class TimeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetDateDto() throws Exception {
        mockMvc.perform(get("/time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.localTime").exists())
                .andExpect(jsonPath("$.offsetTime").exists());
    }

    @Test
    void testGetDateDtoParam() throws Exception {
        String localTime = "12:34:56";
        String offsetTime = "12:34:56+02:00";
        mockMvc.perform(get("/time/withParam")
                        .param("localTime", localTime)
                        .param("offsetTime", offsetTime))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.localTime", is(localTime)))
                .andExpect(jsonPath("$.offsetTime", is(offsetTime)));
    }

    @Test
    void testTimeWithBody() throws Exception {
        LocalTime localTime = LocalTime.of(10, 20, 30);
        OffsetTime offsetTime = OffsetTime.of(10, 20, 30, 0, ZoneOffset.ofHoursMinutes(5, 30));
        TimeDto dto = new TimeDto(localTime, offsetTime);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/time/withBody")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.localTime", is(localTime.toString())))
                .andExpect(jsonPath("$.offsetTime", is(offsetTime.toString())));
    }

    @Test
    void testOffSetDateAndTimeWithParam() throws Exception {
        String offsetTime = "15:45:01+05:30";
        mockMvc.perform(get("/time/offset/withParam")
                        .param("offsetTime", offsetTime))
                .andExpect(status().isOk());
    }

    @Test
    void testOffSetDateAndTime() throws Exception {
        String response = mockMvc.perform(get("/time/offset/get"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        String date = json.asText();
        OffsetTime offsetTime = OffsetTime.parse(date, DateTimeFormatter.ISO_OFFSET_TIME);
        assertTrue(offsetTime.isBefore(OffsetTime.now()));
    }
}