package com.example.calco.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WebDishes {
    @SerializedName("dishes")
    private List<WebProduct> products;

    public List<WebProduct> getProducts() {
        return products;
    }

    public WebDishes(List<WebProduct> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "WebDishes{" +
                "products=" + products +
                '}';
    }
}
