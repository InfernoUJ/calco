package com.example.calco.ui.products.table;

import com.example.calco.logic.business.entities.Food;

import java.io.Serializable;

public class FoodImpactRecordData implements Serializable {
    private Food food;
    private String name;
    private Integer percentage;
    private Integer absoluteValue;
    private Integer imageId;

    public FoodImpactRecordData(Food food, String name, Integer percentage, Integer absoluteValue, Integer imageId) {
        this.food = food;
        this.name = name;
        this.percentage = percentage;
        this.absoluteValue = absoluteValue;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public Integer getAbsoluteValue() {
        return absoluteValue;
    }

    public Integer getImageId() {
        return imageId;
    }

    public Food getFood() { return food; }
}
