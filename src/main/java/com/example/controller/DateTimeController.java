package com.example.controller;

import com.example.model.DateTimeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * @author Deepak
 */
@RestController
@RequestMapping("/datetime")
public class DateTimeController {

    /**
     * @return LocalDateTime in ISO format
     * @see LocalDate
     * @see java.time.format.DateTimeFormatter
     */
    @GetMapping
    public ResponseEntity<DateTimeDto> getDateDto() {
        return ResponseEntity.ok(new DateTimeDto(LocalDateTime.now(), ZonedDateTime.now(), OffsetDateTime.now()));
    }

    /**
     * @param dateTimeDto in ISO format
     * @return LocalDateTime in ISO format
     */
    @GetMapping("/withParam")
    public ResponseEntity<DateTimeDto> getDateDtoParam(DateTimeDto dateTimeDto) {
        return ResponseEntity.ok(dateTimeDto);
    }

    @PostMapping("/withBody")
    public ResponseEntity<DateTimeDto> getDateDtoAsBody(@RequestBody DateTimeDto dateTimeDto, TimeZone timezone) {
        ZonedDateTime zonedDateTime= ZonedDateTime.now();
        return ResponseEntity.ok(dateTimeDto);
    }

}
