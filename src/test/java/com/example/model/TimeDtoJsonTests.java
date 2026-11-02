package com.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("TimeDto JSON Serialization & Deserialization Tests")
class TimeDtoJsonTests {

    @Autowired
    private JacksonTester<TimeDto> json;

    @Test
    @DisplayName("should serialize TimeDto to ISO-8601 time JSON")
    void shouldSerializeTimeDto() throws Exception {
        LocalTime localTime = LocalTime.of(14, 30, 45);
        OffsetTime offsetTime = OffsetTime.of(14, 30, 45, 0, ZoneOffset.ofHoursMinutes(5, 30));
        TimeDto dto = new TimeDto(localTime, offsetTime);

        JsonContent<TimeDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.localTime").isEqualTo("14:30:45");
        assertThat(result).extractingJsonPathStringValue("$.offsetTime").isEqualTo("14:30:45+05:30");
    }

    @Test
    @DisplayName("should deserialize TimeDto from ISO-8601 time JSON")
    void shouldDeserializeTimeDto() throws Exception {
        String content = "{\"localTime\":\"14:30:45\",\"offsetTime\":\"14:30:45+05:30\"}";
        TimeDto dto = json.parseObject(content);

        assertThat(dto.getLocalTime()).isEqualTo(LocalTime.of(14, 30, 45));
        assertThat(dto.getOffsetTime()).isEqualTo(OffsetTime.of(14, 30, 45, 0, ZoneOffset.ofHoursMinutes(5, 30)));
    }
}
