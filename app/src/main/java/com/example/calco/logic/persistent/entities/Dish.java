package com.example.calco.logic.persistent.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dish {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "name")
    public String name;
}
