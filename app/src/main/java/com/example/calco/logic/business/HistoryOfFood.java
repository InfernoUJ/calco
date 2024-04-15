package com.example.calco.logic.business;

import java.time.LocalDate;

public abstract class HistoryOfFood {
    private long id;

    public long getId() {
        return id;
    }

    public Food getFood() {
        return food;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getMilligrams() {
        return milligrams;
    }

    private Food food;
    private LocalDate date;
    private int milligrams;

    public HistoryOfFood(long id, Food food, LocalDate date, int milligrams) {
        this.id = id;
        this.food = food;
        this.date = date;
        this.milligrams = milligrams;
    }
}
