package com.example.calco.logic.persistent.converters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class DateTimeConverterTest {
    private DateTimeConverter converter;
    @Before
    public void setUp() {
        converter = new DateTimeConverter();
    }

    @Test
    public void timeFromUtcMillis() {
        // 2000.07.14 12:30:00 UTC
        Long millis = 963577800000L;
        // Assuming the system default time zone is UTC+02:00
        assertEquals(LocalDateTime.of(2000, 7, 14, 14, 30, 0, 0),
                converter.timeFromUtcMillis(millis));
    }

    @Test
    public void timeToUtcMillis() {
        // 2000.07.14 14:30:00 UTC+04:00
        LocalDateTime dateTime = LocalDateTime.of(2000, 7, 14, 14, 30, 0, 0);
        assertEquals(Long.valueOf(963577800000L), converter.timeToUtcMillis(dateTime));
    }
}