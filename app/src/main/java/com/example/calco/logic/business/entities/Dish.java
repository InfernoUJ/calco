package com.example.calco.logic.business.entities;

import java.util.List;

public class Dish extends Food {
    public static final String DEFAULT_IMAGE = "question_mark";
    private List<DishComponent> components;

    public Dish(long id, String name, List<DishComponent> components, String imageName) {
        super(id, name, getCalories(components), getCarbs(components), getFats(components), getProteins(components), imageName);
        this.components = components;
    }

    public List<DishComponent> getComponents() {
        return components;
    }

    protected static Integer getCalories(List<DishComponent> components) {
        int calories = 0;
        for (DishComponent component : components) {
            calories += component.getProduct().getCalories() * component.getPercent() / 100;
        }
        return calories;
    }

    protected static Integer getCarbs(List<DishComponent> components) {
        int carbs = 0;
        for (DishComponent component : components) {
            carbs += component.getProduct().getCarbs() * component.getPercent() / 100;
        }
        return carbs;
    }

    protected static Integer getFats(List<DishComponent> components) {
        int fats = 0;
        for (DishComponent component : components) {
            fats += component.getProduct().getFats() * component.getPercent() / 100;
        }
        return fats;
    }

    protected static Integer getProteins(List<DishComponent> components) {
        int proteins = 0;
        for (DishComponent component : components) {
            proteins += component.getProduct().getProteins() * component.getPercent() / 100;
        }
        return proteins;
    }
}
