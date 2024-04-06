package com.example.calco.logic.persistent.converters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeConverterTest {
    private ZonedDateTimeConverter converter;
    @Before
    public void setUp() {
        converter = new ZonedDateTimeConverter();
    }

    @Test
    public void zonedFromUtcMillis() {
        // 2000.07.14 12:30:00 UTC
        Long millis = 963577800000L;
        // Assuming the system default time zone is UTC+02:00
        assertEquals(ZonedDateTime.of(2000, 7, 14, 14, 30, 0, 0, ZoneId.systemDefault()),
                converter.zonedFromUtcMillis(millis));
    }

    @Test
    public void zonedToUtcMillis() {
        // 2000.07.14 16:30:00 UTC+04:00
        ZonedDateTime zoned = ZonedDateTime.of(2000, 7, 14, 16, 30, 0, 0, ZoneId.of("UTC+04:00"));
        assertEquals(Long.valueOf(963577800000L), converter.zonedToUtcMillis(zoned));
    }
}