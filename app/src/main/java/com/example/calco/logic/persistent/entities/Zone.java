package com.example.calco.logic.persistent.entities;

import androidx.room.*;

import java.time.ZoneId;

@Entity(indices = @Index(value = {"java_zone_id"}, unique = true))
public class Zone {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "java_zone_id")
    public ZoneId zoneId;
}
