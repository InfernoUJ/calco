package com.example.calco.logic.persistent.dao;

import java.util.List;

public interface BaseDao <T>{
    List<T> getAll();
}
