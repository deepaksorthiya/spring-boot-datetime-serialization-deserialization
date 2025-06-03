package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.time.OffsetTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TimeDto {
    private LocalTime localTime;
    private OffsetTime offsetTime;
}
