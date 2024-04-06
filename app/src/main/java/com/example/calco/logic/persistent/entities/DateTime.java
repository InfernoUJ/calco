package com.example.calco.logic.persistent.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(foreignKeys = @ForeignKey(entity = Zone.class, parentColumns = "uid", childColumns = "zone_id"),
        indices = {@Index(value = {"zone_id"}),
                   @Index(value = {"utc_date_time"}),
                   @Index(value = {"zoned_date_time"})})
public class DateTime {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "utc_date_time")
    public LocalDateTime utcDateTime;

    @ColumnInfo(name = "zoned_date_time")
    public LocalDateTime zonedDateTime;

    @ColumnInfo(name = "zone_id")
    public int zoneId;
}
