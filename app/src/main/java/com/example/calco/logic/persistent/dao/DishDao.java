package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.Dish;

import java.util.List;

@Dao
public interface DishDao {
    @Query("SELECT * FROM dish " +
            "WHERE name LIKE :name ")
    List<Dish> findByName(String name);

    @Query("SELECT * FROM dish " +
            "WHERE uid LIKE :uid " )
    Dish findById(int uid);

    @Insert
    void insertAll(Dish... dishes);

    @Delete
    void delete(Dish dish);
}
