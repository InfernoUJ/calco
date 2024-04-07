package com.example.calco.logic.business;

public class ProductLogic {
    private static final String DEFAULT_IMAGE = "question_mark.png";

    public static void createNewProduct(String name, Integer calories, Integer carbs, Integer fats, Integer proteins) {
        createNewProduct(name, calories, carbs, fats, proteins, DEFAULT_IMAGE);
    }

    public static void createNewProduct(String name, Integer calories, Integer carbs, Integer fats, Integer proteins, String imageName) {
        Product product = new Product(name, calories, carbs, fats, proteins, imageName);
        // Save product to database

    }
}
