package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.PHistoryOfProducts;
import com.example.calco.logic.persistent.entities.PProduct;

import java.util.List;

@Dao
public interface HistoryOfProductsDao extends BaseDao<PHistoryOfProducts> {
    @Query("SELECT PProduct.* FROM PProduct " +
            "JOIN PHistoryOfProducts ON  PHistoryOfProducts.product_id = PProduct.uid " +
            "ORDER BY PHistoryOfProducts.utc_date_time DESC ")
    List<PProduct> getLastUsedProducts();

    @Query("SELECT * FROM PHistoryOfProducts " +
            "WHERE utc_date_time BETWEEN :dateStart AND :dateEnd ")
    List<PHistoryOfProducts> getHistoryInDateDiapason(long dateStart, long dateEnd);

    @Override
    @Query("SELECT * FROM PHistoryOfProducts")
    List<PHistoryOfProducts> getAll();
}
