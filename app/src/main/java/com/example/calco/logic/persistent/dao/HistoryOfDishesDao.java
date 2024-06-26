package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.PHistoryOfDishes;
import com.example.calco.logic.persistent.entities.PDish;

import java.util.List;

@Dao
public interface HistoryOfDishesDao extends BaseDao<PHistoryOfDishes>{
    @Query("SELECT PDish.* FROM PDish " +
            "JOIN PHistoryOfDishes ON  PHistoryOfDishes.dish_id = PDish.uid " +
            "ORDER BY PHistoryOfDishes.utc_date_time DESC ")
    List<PDish> getLastUsedDishes();

    @Query("SELECT * FROM PHistoryOfDishes " +
            "WHERE utc_date_time BETWEEN :dateStart AND :dateEnd ")
    List<PHistoryOfDishes> getHistoryInDateDiapason(long dateStart, long dateEnd);

    @Override
    @Query("SELECT * FROM PHistoryOfDishes")
    List<PHistoryOfDishes> getAll();

}
