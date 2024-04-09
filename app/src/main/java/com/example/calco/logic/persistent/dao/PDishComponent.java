package com.example.calco.logic.persistent.dao;

import androidx.room.ColumnInfo;

public class PDishComponent {
    @ColumnInfo(name = "product_id")
    public long productId;
    @ColumnInfo(name = "percent_content")
    public int percentContent;
}
