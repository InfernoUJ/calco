package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calco.logic.persistent.entities.DishImages;
import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.PHistoryOfDishes;
import com.example.calco.logic.persistent.entities.ProductImages;

import java.util.List;

@Dao
public interface DishImagesDao {
    @Insert
    List<Long> insertAll(DishImages... dishImages);

    @Query("SELECT * FROM DishImages " +
            "WHERE dish_id = :dish_id")
    DishImages getDishImages(long dish_id);

    @Query("UPDATE DishImages " +
            "SET image_id = :imageId " +
            "WHERE dish_id = :dishId")
    int update(long imageId, long dishId);

}
