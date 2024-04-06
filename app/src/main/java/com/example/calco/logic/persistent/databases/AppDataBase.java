package com.example.calco.logic.persistent.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.calco.logic.persistent.converters.*;
import com.example.calco.logic.persistent.dao.*;
import com.example.calco.logic.persistent.entities.*;

@Database(entities = { Product.class, Dish.class, Image.class, ProductImages.class, DishImages.class,
        ProductsInDishes.class, HistoryOfProducts.class, HistoryOfDishes.class },
          version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase dataBaseInstance;
    private static final String dataBaseName = "calco_db";

    public static AppDataBase getInstance(Context ctx) {
        if (dataBaseInstance == null) {
            dataBaseInstance = Room.databaseBuilder(ctx, AppDataBase.class, dataBaseName).build();
        }
        return dataBaseInstance;
    }

    public abstract ProductDao productDao();

    public abstract DishDao dishDao();
}
