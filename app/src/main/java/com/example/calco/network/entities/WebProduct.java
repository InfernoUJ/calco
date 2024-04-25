package com.example.calco.network.entities;

import com.google.gson.annotations.SerializedName;

public class WebProduct {
    @SerializedName("name")
    private String name;

    @SerializedName("caloric")
    private float calories;

    @SerializedName("carbon")
    private float carbs;

    @SerializedName("protein")
    private float proteins;

    @SerializedName("fat")
    private float fats;

    public String getName() {
        return name;
    }

    public float getCalories() {
        return calories;
    }

    public float getCarbs() {
        return carbs;
    }

    public float getProteins() {
        return proteins;
    }

    public float getFats() {
        return fats;
    }

    public WebProduct(String name, float calories, float carbs, float proteins, float fats) {
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
    }

    @Override
    public String toString() {
        return "WebProduct{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", carbs=" + carbs +
                ", proteins=" + proteins +
                ", fats=" + fats +
                '}';
    }
}
