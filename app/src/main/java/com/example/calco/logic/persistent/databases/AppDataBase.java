package com.example.calco.logic.persistent.databases;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calco.logic.persistent.dao.*;
import com.example.calco.logic.persistent.entities.*;

@Database(entities = { PProduct.class, PDish.class, Image.class, ProductImages.class, DishImages.class,
        ProductsInDishes.class, HistoryOfProducts.class, HistoryOfDishes.class },
          version = 2,
        autoMigrations = {
                @AutoMigration(from = 1, to = 2)
        })
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase dataBaseInstance;
    private static final String dataBaseName = "calco_db";

    public static AppDataBase createInstance(Context ctx) {
        if (dataBaseInstance == null) {
            synchronized (AppDataBase.class) {
                // TODO find way to avoid allowMainThreadQueries
                dataBaseInstance = Room.databaseBuilder(ctx, AppDataBase.class, dataBaseName)
                        .allowMainThreadQueries().build();
            }
        }
        return dataBaseInstance;
    }
    public static AppDataBase getInstance() {
        if (dataBaseInstance == null) {
            throw new IllegalStateException("Database has not been created yet.");
        }
        return dataBaseInstance;
    }

    public abstract ProductDao productDao();

    public abstract DishDao dishDao();

    public abstract HistoryOfProductsDao historyOfProductsDao();
}
