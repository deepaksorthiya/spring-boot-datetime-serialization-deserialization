package com.example.controller;

import com.example.model.DateTimeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DateTimeController.class)
@DisplayName("DateTimeController Tests")
class DateTimeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /datetime")
    class GetDateTimeTests {

        @Test
        @DisplayName("should return current date-times in ISO format")
        void shouldReturnCurrentDateTimeDto() throws Exception {
            mockMvc.perform(get("/datetime"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.localDateTime").exists())
                    .andExpect(jsonPath("$.zonedDateTime").exists())
                    .andExpect(jsonPath("$.offsetDateTime").exists());
        }
    }

    @Nested
    @DisplayName("GET /datetime/withParam")
    class GetDateTimeWithParamTests {

        @Test
        @DisplayName("should bind DateTimeDto query parameters and return DateTimeDto")
        void shouldReturnDateTimeDtoFromQueryParams() throws Exception {
            mockMvc.perform(get("/datetime/withParam")
                            .param("localDateTime", "2025-06-15T10:30:00")
                            .param("zonedDateTime", "2025-06-15T10:30:00+02:00[Europe/Paris]")
                            .param("offsetDateTime", "2025-06-15T10:30:00+02:00"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.localDateTime").value("2025-06-15T10:30:00"))
                    .andExpect(jsonPath("$.offsetDateTime").value("2025-06-15T10:30:00+02:00"));
        }
    }

    @Nested
    @DisplayName("POST /datetime/withBody")
    class PostDateTimeWithBodyTests {

        @Test
        @DisplayName("should deserialize DateTimeDto from request body and serialize it back")
        void shouldDeserializeAndSerializeBody() throws Exception {
            LocalDateTime localDateTime = LocalDateTime.of(2025, 6, 15, 10, 30, 0);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneId.of("UTC"));
            OffsetDateTime offsetDateTime = OffsetDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneId.of("UTC").getRules().getOffset(localDateTime));
            DateTimeDto requestDto = new DateTimeDto(localDateTime, zonedDateTime, offsetDateTime);

            String jsonPayload = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post("/datetime/withBody")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.localDateTime").value("2025-06-15T10:30:00"))
                    .andExpect(jsonPath("$.offsetDateTime").value("2025-06-15T10:30:00Z"));
        }

        @Test
        @DisplayName("should return 400 Bad Request when request body is malformed JSON")
        void shouldReturnBadRequestForMalformedJson() throws Exception {
            mockMvc.perform(post("/datetime/withBody")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"localDateTime\": \"invalid-datetime\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        }
    }

    @Nested
    @DisplayName("GET /datetime/offset")
    class GetOffsetDateTimeTests {

        @Test
        @DisplayName("should accept valid OffsetDateTime query parameter")
        void shouldReturnGivenOffsetDateTime() throws Exception {
            String offsetDateTimeStr = "2025-06-15T10:30:00+05:30";
            mockMvc.perform(get("/datetime/offset")
                            .param("offsetDateTime", offsetDateTimeStr))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value(offsetDateTimeStr));
        }

        @Test
        @DisplayName("should return 400 Bad Request for invalid OffsetDateTime")
        void shouldReturnBadRequestForInvalidOffsetDateTime() throws Exception {
            mockMvc.perform(get("/datetime/offset")
                            .param("offsetDateTime", "not-a-datetime"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        }

        @Test
        @DisplayName("should return 400 Bad Request when offsetDateTime param is missing")
        void shouldReturnBadRequestWhenOffsetDateTimeMissing() throws Exception {
            mockMvc.perform(get("/datetime/offset"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        }
    }

    @Nested
    @DisplayName("GET /datetime/zone")
    class GetZonedDateTimeTests {

        @Test
        @DisplayName("should accept valid ZonedDateTime query parameter")
        void shouldReturnGivenZonedDateTime() throws Exception {
            String zonedDateTimeStr = "2025-06-15T10:30:00Z";
            mockMvc.perform(get("/datetime/zone")
                            .param("zonedDateTime", zonedDateTimeStr))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value(zonedDateTimeStr));
        }

        @Test
        @DisplayName("should return 400 Bad Request for invalid ZonedDateTime")
        void shouldReturnBadRequestForInvalidZonedDateTime() throws Exception {
            mockMvc.perform(get("/datetime/zone")
                            .param("zonedDateTime", "invalid-zone"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        }

        @Test
        @DisplayName("should return 400 Bad Request when zonedDateTime param is missing")
        void shouldReturnBadRequestWhenZonedDateTimeMissing() throws Exception {
            mockMvc.perform(get("/datetime/zone"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        }
    }

    @Nested
    @DisplayName("GET /datetime/local")
    class GetLocalDateTimeTests {

        @Test
        @DisplayName("should return 400 Bad Request when pattern dd-MMMM-yyyy cannot supply time components for LocalDateTime")
        void shouldFailWhenPatternLacksTimeComponent() throws Exception {
            mockMvc.perform(get("/datetime/local")
                            .param("localDateTime", "15-June-2025"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        }

        @Test
        @DisplayName("should return 400 Bad Request when localDateTime param is missing")
        void shouldReturnBadRequestWhenMissingParam() throws Exception {
            mockMvc.perform(get("/datetime/local"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        }
    }
}
