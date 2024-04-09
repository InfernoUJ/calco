package com.example.calco.logic.business;

import java.time.LocalDate;

public class HistoryOfProducts {
    private long id;

    public long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getMilligrams() {
        return milligrams;
    }

    private Product product;
    private LocalDate date;
    private int milligrams;

    public HistoryOfProducts(long id, Product product, LocalDate date, int milligrams) {
        this.id = id;
        this.product = product;
        this.date = date;
        this.milligrams = milligrams;
    }
}
