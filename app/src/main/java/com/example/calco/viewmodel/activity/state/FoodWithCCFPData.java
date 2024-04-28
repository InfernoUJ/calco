package com.example.calco.viewmodel.activity.state;

import android.graphics.Bitmap;

import com.example.calco.logic.business.entities.Food;

public abstract class FoodWithCCFPData extends ImageTwoTypesImpl {
    protected String name;
    protected String calories;
    protected String carbs;
    protected String fats;
    protected String proteins;
    protected String imageName;

    public FoodWithCCFPData(String name, String imageName, String calories, String carbs, String fats, String proteins) {
        this.name = name;
        this.imageName = imageName;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
    }

    public abstract Food getFood();

    public String getName() {
        return name;
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

    public String getImageName() {
        return imageName;
    }
}
