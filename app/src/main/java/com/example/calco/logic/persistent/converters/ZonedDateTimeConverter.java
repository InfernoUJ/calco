package com.example.calco.logic.persistent.converters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ZonedDateTimeConverter {
    public ZonedDateTime zonedFromUtcMillis(Long millis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public Long zonedToUtcMillis(ZonedDateTime zoned) {
        return zoned.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }
}
