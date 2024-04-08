package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(foreignKeys = { @ForeignKey(entity = PProduct.class, parentColumns = "uid", childColumns = "product_id"),
                        @ForeignKey(entity = PDish.class, parentColumns = "uid", childColumns = "dish_id")},
        indices = { @Index(value = {"product_id"}),
                    @Index(value = {"dish_id"})},
        primaryKeys = {"dish_id", "product_id"}
)
public class ProductsInDishes {
    @ColumnInfo(name = "dish_id")
    public long dishId;

    @ColumnInfo(name = "product_id")
    public long productId;

    /**
     * Percent of content productId in dishId
     */
    @NonNull
    @ColumnInfo(name = "percent_content")
    public int percentContent;
}
