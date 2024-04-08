package com.example.calco.logic.business;

public class Product {
    public static final String DEFAULT_IMAGE = "question_mark";
    private String name;
    /**
     * Calories per 100g in cal
     */
    private Integer calories;
    private Integer carbs;
    private Integer fats;
    private Integer proteins;
    private String imageName;

    public String getName() {
        return name;
    }

    public Integer getCalories() {
        return calories;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public Integer getFats() {
        return fats;
    }

    public Integer getProteins() {
        return proteins;
    }

    public String getImageName() {
        return imageName;
    }

    /*
     * image name must be without extension
     */
    public Product(String name, Integer calories, Integer carbs, Integer fats, Integer proteins, String imageName) {
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
        this.imageName = imageName;
    }
}
