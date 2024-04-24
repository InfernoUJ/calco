package com.example.calco.viewmodel.activity.state;

import com.example.calco.logic.business.entities.Food;

public abstract class FoodWithCCFPData {
    protected String name;
    protected Integer imageId;
    protected String calories;
    protected String carbs;
    protected String fats;
    protected String proteins;

    public FoodWithCCFPData(String name, Integer imageId, String calories, String carbs, String fats, String proteins) {
        this.name = name;
        this.imageId = imageId;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
    }

    public abstract Food getFood();

    public String getName() {
        return name;
    }

    public Integer getImageId() {
        return imageId;
    }

    public String getCalories() {
        return calories;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getFats() {
        return fats;
    }

    public String getProteins() {
        return proteins;
    }
}
