package com.example.calco.logic.files;

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
}
