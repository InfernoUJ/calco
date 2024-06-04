package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.ProductsInDishes;

import java.util.List;

@Dao
public interface ProductsInDishesDao extends BaseDao<ProductsInDishes> {
    @Query("SELECT product_id, percent_content " +
            "FROM ProductsInDishes " +
            "WHERE dish_id = :dishId ")
    List<PDishComponent> getComponents(long dishId);

    @Delete
    void delete(ProductsInDishes dish);

    @Override
    @Query("SELECT * FROM ProductsInDishes")
    List<ProductsInDishes> getAll();
}
