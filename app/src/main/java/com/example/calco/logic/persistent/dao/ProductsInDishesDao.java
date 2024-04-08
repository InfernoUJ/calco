package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.calco.logic.persistent.entities.ProductsInDishes;

import java.util.List;

@Dao
public interface ProductsInDishesDao {
    @Insert
    List<Long> insertAll(ProductsInDishes... dishes);

    @Delete
    void delete(ProductsInDishes dish);
}
