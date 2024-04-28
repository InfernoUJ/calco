package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calco.logic.persistent.entities.PDish;
import com.example.calco.logic.persistent.entities.PHistoryOfDishes;
import com.example.calco.logic.persistent.entities.ProductImages;

import java.util.List;

@Dao
public interface ProductImagesDao {
    @Insert
    List<Long> insertAll(ProductImages... productImages);

    @Query("SELECT * FROM ProductImages " +
            "WHERE product_id = :productId")
    ProductImages getProductImages(long productId);

    @Update
    int update(ProductImages productImages);
}
