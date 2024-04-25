package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class PDish {
    @PrimaryKey(autoGenerate = true)
    public long uid;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PDish pDish = (PDish) o;
        return uid == pDish.uid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
