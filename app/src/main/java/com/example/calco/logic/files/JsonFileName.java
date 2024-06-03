package com.example.calco.logic.files;

import com.example.calco.logic.persistent.entities.*;
public enum JsonFileName {
    DishImages("dish_images"),
    Images("images"),
    PDish("dishes"),
    PHistoryOfDishes("history_of_dishes"),
    PHistoryOfProducts("history_of_products"),
    PLimits("limits"),
    PProduct("products"),
    ProductImages("product_images"),
    ProductsInDishes("products_in_dishes");

    private final String name;

    JsonFileName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static <T> String getName(Class<T> clazz) {
        if (clazz.equals(DishImages.class)) {
            return DishImages.getName();
        } else if (clazz.equals(Image.class)) {
            return Images.getName();
        } else if (clazz.equals(PDish.class)) {
            return PDish.getName();
        } else if (clazz.equals(PHistoryOfDishes.class)) {
            return PHistoryOfDishes.getName();
        } else if (clazz.equals(PHistoryOfProducts.class)) {
            return PHistoryOfProducts.getName();
        } else if (clazz.equals(PLimit.class)) {
            return PLimits.getName();
        } else if (clazz.equals(PProduct.class)) {
            return PProduct.getName();
        } else if (clazz.equals(ProductImages.class)) {
            return ProductImages.getName();
        } else if (clazz.equals(ProductsInDishes.class)) {
            return ProductsInDishes.getName();
        } else {
            return null;
        }
    }

    public String toString() {
        return name;
    }
}
