package com.example.calco.logic.persistent.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.PProduct;

import java.util.List;

@Dao
public interface HistoryOfDishesDao {
    @Query("SELECT * FROM PDish " +
            "JOIN HistoryOfDishes ON  HistoryOfDishes.dish_id = PDish.uid " +
            "ORDER BY HistoryOfDishes.utc_date_time DESC ")
    List<PDish> getLastUsedDishes();

}
