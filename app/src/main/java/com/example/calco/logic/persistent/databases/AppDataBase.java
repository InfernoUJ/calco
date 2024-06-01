package com.example.calco.logic.persistent.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calco.logic.persistent.dao.*;
import com.example.calco.logic.persistent.entities.*;

@Database(entities = { PProduct.class, PDish.class, Image.class, ProductImages.class, DishImages.class,
        ProductsInDishes.class, PHistoryOfProducts.class, PHistoryOfDishes.class,
        PLimit.class },
          version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase dataBaseInstance;
    private static final String dataBaseName = "calco_db";

    public static AppDataBase createInstance(Context ctx) {
        if (dataBaseInstance == null) {
            synchronized (AppDataBase.class) {
                // TODO find way to avoid allowMainThreadQueries
                dataBaseInstance = Room.databaseBuilder(ctx, AppDataBase.class, dataBaseName)
//                        .fallbackToDestructiveMigration()
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

    public abstract HistoryOfDishesDao historyOfDishesDao();

    public abstract ProductsInDishesDao productsInDishesDao();

    public abstract LimitDao limitDao();

    public abstract ImageDao imageDao();

    public abstract ProductImagesDao productImagesDao();

    public abstract DishImagesDao dishImagesDao();

    public <T> BaseDao<T> baseDao(Class<T> clazz) {
        if (clazz.equals(PProduct.class)) {
            return (BaseDao<T>) productDao();
        } else if (clazz.equals(PDish.class)) {
            return (BaseDao<T>) dishDao();
        } else if (clazz.equals(PHistoryOfProducts.class)) {
            return (BaseDao<T>) historyOfProductsDao();
        } else if (clazz.equals(PHistoryOfDishes.class)) {
            return (BaseDao<T>) historyOfDishesDao();
        } else if (clazz.equals(ProductsInDishes.class)) {
            return (BaseDao<T>) productsInDishesDao();
        } else if (clazz.equals(PLimit.class)) {
            return (BaseDao<T>) limitDao();
        } else if (clazz.equals(Image.class)) {
            return (BaseDao<T>) imageDao();
        } else if (clazz.equals(ProductImages.class)) {
            return (BaseDao<T>) productImagesDao();
        } else if (clazz.equals(DishImages.class)) {
            return (BaseDao<T>) dishImagesDao();
        } else {
            throw new IllegalArgumentException("Unknown entity class: " + clazz);
        }
    }

}
