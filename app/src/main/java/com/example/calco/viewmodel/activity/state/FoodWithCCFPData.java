package com.example.calco.viewmodel.activity.state;

import com.example.calco.logic.business.Product;

public class FoodWithCCFPData {
    private Product product;
    private String name;
    private Integer imageId;
    private String calories;
    private String carbs;
    private String fats;
    private String proteins;

    public FoodWithCCFPData(String name, Integer imageId, String calories, String carbs, String fats, String proteins) {
        this.name = name;
        this.imageId = imageId;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
    }

    public Product getProduct() {
        return product;
    }

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
