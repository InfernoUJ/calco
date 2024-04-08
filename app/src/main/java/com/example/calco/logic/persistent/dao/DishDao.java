package com.example.calco.logic.persistent.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.PDish;

import java.util.List;

@Dao
public interface DishDao {
    @Query("SELECT * FROM PDish " +
            "WHERE name LIKE :name ")
    LiveData<List<PDish>> findByName(String name);

    @Query("SELECT * FROM PDish " +
            "WHERE uid LIKE :uid " )
    LiveData<PDish> findById(long uid);

    @Insert
    List<Long> insertAll(PDish... dishes);

    @Delete
    void delete(PDish dish);
}
