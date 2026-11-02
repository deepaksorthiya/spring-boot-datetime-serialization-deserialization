package com.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("DateDto JSON Serialization & Deserialization Tests")
class DateDtoJsonTests {

    @Autowired
    private JacksonTester<DateDto> json;

    @Test
    @DisplayName("should serialize DateDto to ISO-8601 date JSON")
    void shouldSerializeDateDto() throws Exception {
        DateDto dto = new DateDto(LocalDate.of(2025, 1, 15));
        JsonContent<DateDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.localDate").isEqualTo("2025-01-15");
    }

    @Test
    @DisplayName("should deserialize DateDto from ISO-8601 date JSON")
    void shouldDeserializeDateDto() throws Exception {
        String content = "{\"localDate\":\"2025-01-15\"}";
        DateDto dto = json.parseObject(content);

        assertThat(dto.getLocalDate()).isEqualTo(LocalDate.of(2025, 1, 15));
    }
}
