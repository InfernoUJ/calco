package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.HistoryOfDishes;
import com.example.calco.logic.persistent.entities.PHistoryOfProducts;
import com.example.calco.logic.persistent.entities.PDish;

import java.util.List;

@Dao
public interface HistoryOfDishesDao {
    @Query("SELECT PDish.* FROM PDish " +
            "JOIN HistoryOfDishes ON  HistoryOfDishes.dish_id = PDish.uid " +
            "ORDER BY HistoryOfDishes.utc_date_time DESC ")
    List<PDish> getLastUsedDishes();

//    @Query("SELECT * FROM PHistoryOfProducts " +
//            "WHERE utc_date_time BETWEEN :dateStart AND :dateEnd ")
//    List<PHistoryOfProducts> getHistoryInDateDiapason(long dateStart, long dateEnd);

    @Insert
    List<Long> insertAll(HistoryOfDishes... history);

}
