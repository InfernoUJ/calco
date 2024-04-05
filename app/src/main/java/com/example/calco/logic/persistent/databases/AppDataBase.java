package com.example.calco.logic.persistent.databases;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calco.logic.persistent.dao.DishDao;
import com.example.calco.logic.persistent.dao.ProductDao;
import com.example.calco.logic.persistent.entities.Dish;
import com.example.calco.logic.persistent.entities.DishImages;
import com.example.calco.logic.persistent.entities.HistoryOfDishes;
import com.example.calco.logic.persistent.entities.HistoryOfProducts;
import com.example.calco.logic.persistent.entities.Image;
import com.example.calco.logic.persistent.entities.Product;
import com.example.calco.logic.persistent.entities.ProductImages;
import com.example.calco.logic.persistent.entities.ProductsInDishes;

@Database(entities = { Product.class, Dish.class, Image.class, ProductImages.class, DishImages.class,
                    ProductsInDishes.class, HistoryOfProducts.class, HistoryOfDishes.class },
          version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract DishDao dishDao();
}
