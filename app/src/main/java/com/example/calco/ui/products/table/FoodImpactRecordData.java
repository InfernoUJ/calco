package com.example.calco.ui.products.table;

import com.example.calco.logic.business.entities.Food;
import com.example.calco.viewmodel.activity.state.ImageTwoTypesImpl;

import java.io.Serializable;

public class FoodImpactRecordData extends ImageTwoTypesImpl implements Serializable {
    private Food food;
    private String name;
    private String imageName;
    private Integer percentage;
    private Integer absoluteValue;

    public FoodImpactRecordData(Food food, String name, Integer percentage, Integer absoluteValue) {
        this.food = food;
        this.name = name;
        this.imageName = food.getImageName();
        this.percentage = percentage;
        this.absoluteValue = absoluteValue;
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

    public Food getFood() { return food; }

    public String getImageName() {
        return imageName;
    }
}
