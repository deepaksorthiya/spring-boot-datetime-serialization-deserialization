package com.example.controller;

import com.example.model.TimeDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

/**
 * @author Deepak
 */
@RestController
@RequestMapping("/time")
public class TimeController {

    /**
     * @return TimeDto in ISO format
     * @see LocalTime
     * @see java.time.format.DateTimeFormatter
     */
    @GetMapping
    public ResponseEntity<TimeDto> getDateDto() {
        return ResponseEntity.ok(new TimeDto(LocalTime.now()));
    }

    /**
     * @param time in ISO format
     * @return time in ISO format
     */
    @GetMapping("/withParam")
    public ResponseEntity<TimeDto> getDateDtoParam(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        return ResponseEntity.ok(new TimeDto(time));
    }
}
