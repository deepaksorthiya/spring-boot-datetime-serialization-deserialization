package com.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("DateTimeDto JSON Serialization & Deserialization Tests")
class DateTimeDtoJsonTests {

    @Autowired
    private JacksonTester<DateTimeDto> json;

    @Test
    @DisplayName("should serialize DateTimeDto to ISO-8601 date-time JSON")
    void shouldSerializeDateTimeDto() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(2025, 6, 15, 10, 30, 0);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneId.of("Europe/Paris"));
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneOffset.ofHours(2));
        DateTimeDto dto = new DateTimeDto(localDateTime, zonedDateTime, offsetDateTime);

        JsonContent<DateTimeDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.localDateTime").isEqualTo("2025-06-15T10:30:00");
        assertThat(result).extractingJsonPathStringValue("$.zonedDateTime").contains("2025-06-15T10:30:00");
        assertThat(result).extractingJsonPathStringValue("$.offsetDateTime").isEqualTo("2025-06-15T10:30:00+02:00");
    }

    @Test
    @DisplayName("should deserialize DateTimeDto from ISO-8601 date-time JSON")
    void shouldDeserializeDateTimeDto() throws Exception {
        String content = "{\"localDateTime\":\"2025-06-15T10:30:00\"," +
                         "\"zonedDateTime\":\"2025-06-15T10:30:00+02:00[Europe/Paris]\"," +
                         "\"offsetDateTime\":\"2025-06-15T10:30:00+02:00\"}";
        DateTimeDto dto = json.parseObject(content);

        assertThat(dto.getLocalDateTime()).isEqualTo(LocalDateTime.of(2025, 6, 15, 10, 30, 0));
        assertThat(dto.getZonedDateTime()).isEqualTo(ZonedDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneId.of("Europe/Paris")));
        assertThat(dto.getOffsetDateTime()).isEqualTo(OffsetDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneOffset.ofHours(2)));
    }
}
