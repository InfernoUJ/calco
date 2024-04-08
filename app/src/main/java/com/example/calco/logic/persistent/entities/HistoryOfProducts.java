package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = PProduct.class, parentColumns = "uid", childColumns = "product_id")},
        indices = {@Index(value = {"product_id"}),
                   @Index(value = {"utc_date_time"})})
public class HistoryOfProducts {
    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "product_id")
    public long productId;

    @NonNull
    @ColumnInfo(name = "utc_date_time")
    public int utcDateTime;

    @NonNull
    @ColumnInfo(name = "milligrams")
    public int milligrams;
}
