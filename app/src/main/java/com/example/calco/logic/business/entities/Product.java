package com.example.calco.logic.business.entities;

public class Product extends Food {
    public static final String DEFAULT_IMAGE = "question_mark";

    public Product(long id, String name, Integer calories, Integer carbs, Integer fats, Integer proteins, String imageName) {
        super(id, name, calories, carbs, fats, proteins, imageName);
    }

    public Product(long newId, Product productWithoutId) {
        super(newId, productWithoutId.name, productWithoutId.calories, productWithoutId.carbs,
                productWithoutId.fats, productWithoutId.proteins, productWithoutId.imageName);
    }
}
