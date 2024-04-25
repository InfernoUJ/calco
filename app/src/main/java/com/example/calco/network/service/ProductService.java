package com.example.calco.network.service;

import com.example.calco.network.entities.WebDishes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    @GET("apiFood.php")
    Call<WebDishes> getDishes(@Query("name") String name, @Query("lang") String lang);
}
