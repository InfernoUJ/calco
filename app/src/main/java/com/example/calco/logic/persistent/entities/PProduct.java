package com.example.calco.logic.persistent.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class PProduct {
    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "name")
    public String name;

    /**
     * Calories per 100g in cal
     */
    @ColumnInfo(name = "calories")
    public Integer calories;

    /**
     * Carbs per 100g in milligrams
     */
    @ColumnInfo(name = "carbs")
    public Integer carbs;

    /**
     * Fats per 100g in milligrams
     */
    @ColumnInfo(name = "fats")
    public Integer fats;

    /**
     * Proteins per 100g in milligrams
     */
    @ColumnInfo(name = "proteins")
    public Integer proteins;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PProduct pProduct = (PProduct) o;
        return uid == pProduct.uid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
