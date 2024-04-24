package com.example.calco.logic.business.entities;

import java.time.LocalDate;

public class HistoryOfDishes extends HistoryOfFood {

    public Dish getDish() {
        return (Dish)food;
    }

    public HistoryOfDishes(long id, Dish dish, LocalDate date, int milligrams) {
        super(id, dish, date, milligrams);
    }
}
