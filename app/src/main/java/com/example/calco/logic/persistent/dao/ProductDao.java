package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product " +
            "WHERE name LIKE :name ")
    List<Product> findByName(String name);

    @Query("SELECT * FROM product " +
            "WHERE uid LIKE :uid " )
    Product findById(int uid);

    @Insert
    void insertAll(Product... users);

    @Delete
    void delete(Product user);
}
