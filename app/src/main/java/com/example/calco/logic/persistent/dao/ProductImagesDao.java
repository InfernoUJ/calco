package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.PHistoryOfDishes;

import java.util.List;

@Dao
public interface ProductImagesDao {
    @Insert
    List<Long> insertAll(ProductImagesDao... history);
}
