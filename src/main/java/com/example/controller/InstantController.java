package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/instant")
public class InstantController {

    @GetMapping
    public ResponseEntity<Instant> instant(@RequestParam Instant instant) {
        return ResponseEntity.ok(instant);
    }

    @GetMapping("/get")
    public ResponseEntity<Instant> getInstant() {
        return ResponseEntity.ok(Instant.now());
    }
}
