package com.example.calco.logic.persistent.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.PProduct;

import java.util.List;

@Dao
public interface DishDao extends BaseDao<PDish> {
    @Query("SELECT * FROM PDish " +
            "WHERE name LIKE :name ")
    List<PDish> findByName(String name);

    @Query("SELECT * FROM PDish " +
            "WHERE uid = :uid " )
    PDish findById(long uid);

    @Delete
    void delete(PDish dish);

    @Query("SELECT * FROM PDish " +
            "ORDER BY name ")
    List<PDish> getDishesAlphabetical();

    @Update
    int updateDish(PDish dish);

    @Override
    @Query("SELECT * FROM PDish")
    List<PDish> getAll();
}
