package com.example.calco.logic.business;

import java.util.List;

public class Dish extends Food {
    public static final String DEFAULT_IMAGE = "question_mark";
    private long id;
    private String name;
    private String imageName;
    private List<DishComponent> components;

    public Dish(long id, String name, List<DishComponent> components, String imageName) {
        this.id = id;
        this.name = name;
        this.components = components;
        this.imageName = imageName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public List<DishComponent> getComponents() {
        return components;
    }

    public Integer getCalories() {
        int calories = 0;
        for (DishComponent component : components) {
            calories += component.getProduct().getCalories() * component.getPercent() / 100;
        }
        return calories;
    }

    public Integer getCarbs() {
        int carbs = 0;
        for (DishComponent component : components) {
            carbs += component.getProduct().getCarbs() * component.getPercent() / 100;
        }
        return carbs;
    }

    public Integer getFats() {
        int fats = 0;
        for (DishComponent component : components) {
            fats += component.getProduct().getFats() * component.getPercent() / 100;
        }
        return fats;
    }

    public Integer getProteins() {
        int proteins = 0;
        for (DishComponent component : components) {
            proteins += component.getProduct().getProteins() * component.getPercent() / 100;
        }
        return proteins;
    }
}
