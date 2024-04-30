package com.example.calco.logic.business.entities;

import java.time.LocalDate;

public abstract class HistoryOfFood {
    protected long id;
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

    public void setMilligrams(int milligrams) {
        this.milligrams = milligrams;
    }

    protected Food food;
    protected LocalDate date;
    protected int milligrams;

    public HistoryOfFood(long id, Food food, LocalDate date, int milligrams) {
        this.id = id;
        this.food = food;
        this.date = date;
        this.milligrams = milligrams;
    }
}
