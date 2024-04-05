package com.example.calco.logic.persistent.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Product.class, parentColumns = "uid", childColumns = "product_id"),
        indices = @Index(value = {"product_id"}, unique = true))
public class HistoryOfProducts {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "product_id")
    public int productId;

    @ColumnInfo(name = "date_time")
    public String zonedDateTime;

    @ColumnInfo(name = "milligrams")
    public int milligrams;
}
