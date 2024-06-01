package com.example.calco.logic.persistent.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.calco.logic.persistent.entities.Image;
import com.example.calco.logic.persistent.entities.PDish;

import java.util.List;

@Dao
public interface ImageDao extends BaseDao<Image> {
    @Query("SELECT * FROM Image " +
            "WHERE uid = :uid " )
    Image findById(long uid);

    @Query("SELECT Image.* FROM Image " +
            "WHERE name = :path " )
    List<Image> getImageByPath(String path);

    @Insert
    List<Long> insertAll(Image... dishes);

    @Delete
    void delete(Image dish);

    @Override
    @Query("SELECT * FROM Image")
    List<Image> getAll();
}
