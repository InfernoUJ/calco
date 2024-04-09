package com.example.calco.logic.persistent.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.HistoryOfProducts;
import com.example.calco.logic.persistent.entities.PProduct;

import java.util.List;

@Dao
public interface HistoryOfProductsDao {
    @Query("SELECT * FROM PProduct " +
            "JOIN HistoryOfProducts ON  HistoryOfProducts.product_id = PProduct.uid " +
            "ORDER BY HistoryOfProducts.utc_date_time DESC ")
    List<PProduct> getLastUsedProducts();

}
