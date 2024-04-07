package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PDish {
    @PrimaryKey
    public int uid;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;
}
