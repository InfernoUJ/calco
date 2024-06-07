package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.calco.logic.persistent.entities.ProductImages;

import java.util.List;

@Dao
public interface ProductImagesDao extends BaseDao<ProductImages> {

    @Query("SELECT * FROM ProductImages " +
            "WHERE product_id = :productId")
    ProductImages getProductImages(long productId);

    @Query("UPDATE ProductImages " +
            "SET image_id = :imageId " +
            "WHERE product_id = :productId")
    int update(long imageId, long productId);

    @Override
    @Query("SELECT * FROM ProductImages")
    List<ProductImages> getAll();
}
