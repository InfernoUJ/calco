package com.example.calco.logic.persistent.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(foreignKeys = {@ForeignKey(entity = Dish.class, parentColumns = "uid", childColumns = "dish_id"),
                        @ForeignKey(entity = Image.class, parentColumns = "uid", childColumns = "image_id")},
        indices = {@Index(value = {"dish_id"}, unique = true)},
        primaryKeys = {"image_id", "dish_id"}
)
public class DishImages {
    @NonNull
    @ColumnInfo(name = "image_id")
    public String imageId;

    @NonNull
    @ColumnInfo(name = "dish_id")
    public int dishId;
}
