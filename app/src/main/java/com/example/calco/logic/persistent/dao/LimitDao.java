package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.PLimit;

import java.util.List;

@Dao
public interface LimitDao extends BaseDao<PLimit> {
    @Query("SELECT * FROM PLimit " +
            "WHERE type = :type " +
            "ORDER BY uid DESC ")
    List<PLimit> findLimitByType(String type);

    @Query("UPDATE PLimit " +
            "SET calories = :calories, carbs = :carbs, fats = :fats, proteins = :proteins " +
            "WHERE type = :type ")
    void updateLimit(String type, Integer calories, Integer carbs, Integer fats, Integer proteins);

    @Insert
    Long insert(PLimit limits);

    @Override
    @Query("SELECT * FROM PLimit")
    List<PLimit> getAll();
}
