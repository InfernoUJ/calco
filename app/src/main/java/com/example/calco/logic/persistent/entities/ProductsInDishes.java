package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(foreignKeys = { @ForeignKey(entity = Product.class, parentColumns = "uid", childColumns = "product_id"),
                        @ForeignKey(entity = Dish.class, parentColumns = "uid", childColumns = "dish_id")},
        indices = { @Index(value = {"product_id"}, unique = true),
                    @Index(value = {"dish_id"}, unique = true)},
        primaryKeys = {"dish_id", "product_id"}
)
public class ProductsInDishes {
    @ColumnInfo(name = "dish_id")
    public int dishId;

    @ColumnInfo(name = "product_id")
    public int productId;

    /**
     * Percent of content productId in dishId
     */
    @NonNull
    @ColumnInfo(name = "percent_content")
    public int percentContent;
}
