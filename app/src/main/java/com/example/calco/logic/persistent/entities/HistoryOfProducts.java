package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Product.class, parentColumns = "uid", childColumns = "product_id"),
                       @ForeignKey(entity = DateTime.class, parentColumns = "uid", childColumns = "date_time_id")},
        indices = {@Index(value = {"product_id"}, unique = true),
                   @Index(value = {"date_time_id"}, unique = true)})
public class HistoryOfProducts {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "product_id")
    public int productId;

    @NonNull
    @ColumnInfo(name = "date_time_id")
    public int dateTimeId;

    @NonNull
    @ColumnInfo(name = "milligrams")
    public int milligrams;
}