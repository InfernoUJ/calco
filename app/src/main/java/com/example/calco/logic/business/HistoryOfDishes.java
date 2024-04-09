package com.example.calco.logic.business;

import java.time.LocalDate;

public class HistoryOfDishes {
    private long id;

    public long getId() {
        return id;
    }

    public Dish getDish() {
        return dish;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getMilligrams() {
        return milligrams;
    }

    private Dish dish;
    private LocalDate date;
    private int milligrams;

    public HistoryOfDishes(long id, Dish dish, LocalDate date, int milligrams) {
        this.id = id;
        this.dish = dish;
        this.date = date;
        this.milligrams = milligrams;
    }
}
