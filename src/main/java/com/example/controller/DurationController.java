package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/duration")
public class DurationController {

    /**
     * @param duration
     * @return
     * @see Duration#parse(CharSequence)
     */

    @GetMapping
    public ResponseEntity<Duration> duration(@RequestParam Duration duration) {
        return ResponseEntity.ok(duration);
    }

    @GetMapping("/get")
    public ResponseEntity<Duration> getDuration() {
        return ResponseEntity.ok(Duration.of(1, ChronoUnit.SECONDS));
    }
}
