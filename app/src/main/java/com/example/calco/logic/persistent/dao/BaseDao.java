package com.example.calco.logic.persistent.dao;

import androidx.room.Insert;

import java.util.List;

public interface BaseDao <T>{
    List<T> getAll();
    @Insert
    List<Long> insertAll(T... entities);
}
