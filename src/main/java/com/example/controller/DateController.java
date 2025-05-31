package com.example.controller;

import com.example.model.DateDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author Deepak
 */
@RestController
@RequestMapping("/date")
public class DateController {

    /**
     * @return LocalDate in ISO format
     * @see LocalDate
     * @see java.time.format.DateTimeFormatter
     */
    @GetMapping
    public ResponseEntity<DateDto> getDateDto() {
        return ResponseEntity.ok(new DateDto(LocalDate.now()));
    }

    /**
     * @param date in ISO format
     * @return
     */
    @GetMapping("/withParam")
    public ResponseEntity<DateDto> getDateDtoParam(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(new DateDto(date));
    }
}
