package com.example.calco.logic.business;

import java.time.LocalDate;

public class HistoryOfDishes extends HistoryOfFood {

    public Dish getDish() {
        return dish;
    }

    private Dish dish;

    public HistoryOfDishes(long id, Dish dish, LocalDate date, int milligrams) {
        super(id, dish, date, milligrams);
    }
}
