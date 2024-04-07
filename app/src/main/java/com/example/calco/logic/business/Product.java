package com.example.calco.logic.business;

public class Product {
    private static final String DEFAULT_IMAGE = "question_mark.png";
    private String name;
    /**
     * Calories per 100g in milligrams
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

    public Product(String name, Integer calories, Integer carbs, Integer fats, Integer proteins, String imageName) {
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
        this.imageName = imageName;
    }

    public static void createNewProduct(String name, Integer calories, Integer carbs, Integer fats, Integer proteins) {
        createNewProduct(name, calories, carbs, fats, proteins, DEFAULT_IMAGE);
    }

    public static void createNewProduct(String name, Integer calories, Integer carbs, Integer fats, Integer proteins, String imageName) {
        Product product = new Product(name, calories, carbs, fats, proteins, imageName);
        // Save product to database
    }
}
