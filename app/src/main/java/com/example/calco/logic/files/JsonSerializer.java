package com.example.calco.logic.files;

import com.example.calco.logic.persistent.dao.BaseDao;
import com.example.calco.logic.persistent.databases.AppDataBase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonSerializer {
    public static <T> JsonElement serialize(Class<T> clazz){
        Gson gson = new Gson();
        AppDataBase db = AppDataBase.getInstance();

        BaseDao<T> dao = db.baseDao(clazz);
        return gson.toJsonTree(dao.getAll());
    }

}
