package com.example.calco.logic.persistent.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.PProduct;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM PProduct " +
            "WHERE name LIKE :name ")
    List<PProduct> findByName(String name);

    @Query("SELECT * FROM PProduct " +
            "WHERE uid = :uid " )
    PProduct findById(long uid);

    @Query("SELECT * FROM PProduct " +
            "WHERE name = :name " +
            "AND calories = :calories " +
            "AND carbs = :carbs " +
            "AND fats = :fats " +
            "AND proteins = :proteins ")
    List<PProduct> findByAll(String name, int calories, int carbs, int fats, int proteins);

    @Insert
    List<Long> insertAll(PProduct... users);

    @Delete
    void delete(PProduct user);

    @Query("SELECT * FROM PProduct " +
            "ORDER BY name ")
    List<PProduct> getProductsAlphabetical();

}
