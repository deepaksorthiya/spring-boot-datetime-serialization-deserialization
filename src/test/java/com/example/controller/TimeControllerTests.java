package com.example.controller;

import com.example.model.TimeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TimeController.class)
@DisplayName("TimeController Tests")
class TimeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /time")
    class GetTimeTests {

        @Test
        @DisplayName("should return current LocalTime and OffsetTime in ISO format")
        void shouldReturnCurrentTimeDto() throws Exception {
            mockMvc.perform(get("/time"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.localTime").isString())
                    .andExpect(jsonPath("$.offsetTime").isString())
                    .andExpect(jsonPath("$.localTime", matchesPattern("^\\d{2}:\\d{2}(:\\d{2}(\\.\\d+)?)?$")))
                    .andExpect(jsonPath("$.offsetTime", matchesPattern("^\\d{2}:\\d{2}(:\\d{2}(\\.\\d+)?)?([+-]\\d{2}:\\d{2}|Z)$")));
        }
    }

    @Nested
    @DisplayName("GET /time/withParam")
    class GetTimeWithParamTests {

        @Test
        @DisplayName("should accept valid LocalTime and OffsetTime query parameters")
        void shouldReturnGivenTimeParams() throws Exception {
            String localTime = "12:34:56";
            String offsetTime = "12:34:56+02:00";

            mockMvc.perform(get("/time/withParam")
                            .param("localTime", localTime)
                            .param("offsetTime", offsetTime))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.localTime", is(localTime)))
                    .andExpect(jsonPath("$.offsetTime", is(offsetTime)));
        }

        @Test
        @DisplayName("should return 400 Bad Request when localTime is invalid")
        void shouldReturnBadRequestWhenLocalTimeInvalid() throws Exception {
            mockMvc.perform(get("/time/withParam")
                            .param("localTime", "25:61:99")
                            .param("offsetTime", "12:34:56+02:00"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.title").value("Bad Request"));
        }

        @Test
        @DisplayName("should return 400 Bad Request when offsetTime is invalid")
        void shouldReturnBadRequestWhenOffsetTimeInvalid() throws Exception {
            mockMvc.perform(get("/time/withParam")
                            .param("localTime", "12:34:56")
                            .param("offsetTime", "invalid-offset-time"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.title").value("Bad Request"));
        }

        @Test
        @DisplayName("should return 400 Bad Request when parameters are missing")
        void shouldReturnBadRequestWhenParamsMissing() throws Exception {
            mockMvc.perform(get("/time/withParam"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("POST /time/withBody")
    class PostTimeWithBodyTests {

        @Test
        @DisplayName("should deserialize TimeDto from JSON body and serialize it back")
        void shouldAcceptAndReturnTimeDtoInBody() throws Exception {
            LocalTime localTime = LocalTime.of(10, 20, 30);
            OffsetTime offsetTime = OffsetTime.of(10, 20, 30, 0, ZoneOffset.ofHoursMinutes(5, 30));
            TimeDto dto = new TimeDto(localTime, offsetTime);
            String jsonPayload = objectMapper.writeValueAsString(dto);

            mockMvc.perform(post("/time/withBody")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.localTime", is(localTime.toString())))
                    .andExpect(jsonPath("$.offsetTime", is(offsetTime.toString())));
        }

        @Test
        @DisplayName("should return 400 Bad Request when body contains malformed JSON")
        void shouldReturnBadRequestForMalformedBody() throws Exception {
            mockMvc.perform(post("/time/withBody")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"localTime\": \"invalid-time\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("GET /time/offset/withParam")
    class GetOffsetTimeWithParamTests {

        @ParameterizedTest(name = "should parse input {0} and return serialized offset time {1}")
        @CsvSource({
                "15:45:01+05:30, 15:45:01+05:30",
                "00:00:01Z, 00:00:01Z",
                "00:00:00Z, 00:00Z",
                "23:59:59-04:00, 23:59:59-04:00"
        })
        @DisplayName("should accept valid OffsetTime parameter and return it in JSON")
        void shouldReturnGivenOffsetTime(String inputOffsetTime, String expectedOffsetTime) throws Exception {
            mockMvc.perform(get("/time/offset/withParam")
                            .param("offsetTime", inputOffsetTime))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value(expectedOffsetTime));
        }

        @Test
        @DisplayName("should return 400 Bad Request for invalid offset time parameter")
        void shouldReturnBadRequestForInvalidOffsetTime() throws Exception {
            mockMvc.perform(get("/time/offset/withParam")
                            .param("offsetTime", "invalid-offset"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.title").value("Bad Request"));
        }

        @Test
        @DisplayName("should return 400 Bad Request when offsetTime parameter is missing")
        void shouldReturnBadRequestWhenOffsetTimeMissing() throws Exception {
            mockMvc.perform(get("/time/offset/withParam"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("GET /time/offset/get")
    class GetOffsetTimeTests {

        @Test
        @DisplayName("should return current OffsetTime in ISO format")
        void shouldReturnCurrentOffsetTime() throws Exception {
            mockMvc.perform(get("/time/offset/get"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$", matchesPattern("^\\d{2}:\\d{2}(:\\d{2}(\\.\\d+)?)?([+-]\\d{2}:\\d{2}|Z)$")));
        }
    }
}