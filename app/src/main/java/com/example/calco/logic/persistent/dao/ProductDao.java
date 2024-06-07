package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calco.logic.persistent.entities.PProduct;

import java.util.List;

@Dao
public interface ProductDao extends BaseDao<PProduct> {
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

    @Delete
    void delete(PProduct user);

    @Query("SELECT * FROM PProduct " +
            "ORDER BY name ")
    List<PProduct> getProductsAlphabetical();

    @Update
    int updateProduct(PProduct product);

    @Override
    @Query("SELECT * FROM PProduct")
    List<PProduct> getAll();
}
