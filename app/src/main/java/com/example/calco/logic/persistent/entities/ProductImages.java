package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(foreignKeys = { @ForeignKey(entity = PProduct.class, parentColumns = "uid", childColumns = "product_id"),
                        @ForeignKey(entity = Image.class, parentColumns = "uid", childColumns = "image_id")},
        indices = {@Index(value = {"product_id"}, unique = true)},
        primaryKeys = {"image_id", "product_id"}
)
public class ProductImages {
    @NonNull
    @ColumnInfo(name = "image_id")
    public String imageId;

    @NonNull
    @ColumnInfo(name = "product_id")
    public long productId;
}
