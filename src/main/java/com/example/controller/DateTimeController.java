package com.example.controller;

import com.example.model.DateTimeDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.TimeZone;

/**
 * @author Deepak
 */
@CrossOrigin
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
        ZonedDateTime utc = dateTimeDto.getOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC"));
        return ResponseEntity.ok(dateTimeDto);
    }

    @PostMapping("/withBody")
    public ResponseEntity<DateTimeDto> getDateDtoAsBody(@RequestBody DateTimeDto dateTimeDto, TimeZone timezone) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        return ResponseEntity.ok(dateTimeDto);
    }

    @GetMapping("/local")
    public ResponseEntity<LocalDateTime> localDateAndTime(@RequestParam @DateTimeFormat(pattern = "dd-MMMM-yyyy") LocalDateTime localDateTime) {
        return ResponseEntity.ok(localDateTime);
    }

    @GetMapping("/offset")
    public ResponseEntity<OffsetDateTime> offSetDateAndTime(@RequestParam OffsetDateTime offsetDateTime) {
        return ResponseEntity.ok(offsetDateTime);
    }

    @GetMapping("/zone")
    public ResponseEntity<ZonedDateTime> zoneDateAndTime(@RequestParam ZonedDateTime zonedDateTime) {
        return ResponseEntity.ok(zonedDateTime);
    }

}
