package com.example.calco.logic.business;

public class DishComponent {
    private Product product;
    private int percent;

    public DishComponent(Product product, int percent) {
        this.product = product;
        this.percent = percent;
    }

    public Product getProduct() {
        return product;
    }

    public int getPercent() {
        return percent;
    }
}
