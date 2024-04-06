package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Dish.class, parentColumns = "uid", childColumns = "dish_id")},
        indices = {@Index(value = {"dish_id"}, unique = true),
                   @Index(value = {"utc_date_time"})})
public class HistoryOfDishes {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "dish_id")
    public int dishId;

    @NonNull
    @ColumnInfo(name = "utc_date_time")
    public long utcDateTime;

    @NonNull
    @ColumnInfo(name = "milligrams")
    public int milligrams;
}
