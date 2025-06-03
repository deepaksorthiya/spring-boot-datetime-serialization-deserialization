package com.example.controller;

import com.example.model.TimeDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Deepak
 */
@RestController
@RequestMapping("/time")
public class TimeController {

    /**
     * @return TimeDto in ISO format
     * @see LocalTime
     * @see DateTimeFormatter
     */
    @GetMapping
    public ResponseEntity<TimeDto> getDateDto() {
        return ResponseEntity.ok(new TimeDto(LocalTime.now(), OffsetTime.now()));
    }

    /**
     * @param localTime
     * @param offsetTime
     * @return
     */
    @GetMapping("/withParam")
    public ResponseEntity<TimeDto> getDateDtoParam(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime localTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) OffsetTime offsetTime) {
        return ResponseEntity.ok(new TimeDto(localTime, offsetTime));
    }

    @PostMapping("/withBody")
    public ResponseEntity<TimeDto> timeWithBody(@RequestBody TimeDto timeDto) {
        return ResponseEntity.ok(timeDto);
    }

    @GetMapping("/offset/withParam")
    public ResponseEntity<OffsetTime> offSetDateAndTime(@RequestParam OffsetTime offsetTime) {
        return ResponseEntity.ok(offsetTime);
    }

    @GetMapping("/offset/get")
    public ResponseEntity<OffsetTime> offSetDateAndTime() {
        return ResponseEntity.ok(OffsetTime.now());
    }
}
