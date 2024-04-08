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
    LiveData<List<PProduct>> findByName(String name);

    @Query("SELECT * FROM PProduct " +
            "WHERE uid LIKE :uid " )
    LiveData<PProduct> findById(long uid);

    @Insert
    List<Long> insertAll(PProduct... users);

    @Delete
    void delete(PProduct user);

    @Query("SELECT * FROM PProduct " +
            "ORDER BY uid DESC ")
    List<PProduct> getLastUsedProducts();
}
