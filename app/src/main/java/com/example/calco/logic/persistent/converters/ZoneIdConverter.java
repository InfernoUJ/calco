package com.example.calco.logic.persistent.converters;

import androidx.room.TypeConverter;

import java.time.ZoneId;

public class ZoneIdConverter {
    @TypeConverter
    public ZoneId fromZoneCode(String zoneCode) {
        return ZoneId.of(zoneCode);
    }

    @TypeConverter
    public String toZoneCode(ZoneId zoneId) {
        return zoneId.getId();
    }
}
