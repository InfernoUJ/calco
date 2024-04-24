package com.example.calco.logic.business.entities;

import java.time.LocalDate;

public class HistoryOfProducts  extends HistoryOfFood {
    public Product getProduct() {
        return (Product)food;
    }

    public HistoryOfProducts(long id, Product product, LocalDate date, int milligrams) {
        super(id, product, date, milligrams);
    }
}
