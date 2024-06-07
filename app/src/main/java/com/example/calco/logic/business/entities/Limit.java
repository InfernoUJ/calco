package com.example.calco.logic.business.entities;

public class Limit {
    private static final int DEFAULT_CALORIES_PER_DAY = 2000_000;
    private static final int DEFAULT_CARBS_PER_DAY = 250_000;
    private static final int DEFAULT_FATS_PER_DAY = 70_000;
    private static final int DEFAULT_PROTEINS_PER_DAY = 50_000;
    private static final int DAYS_IN_WEEK = 7;
    private LimitType type;

    /**
     * Calories per 100g in cal
     */
    private Integer calories;

    /**
     * Carbs per 100g in milligrams
     */
    private Integer carbs;

    /**
     * Fats per 100g in milligrams
     */
    private Integer fats;

    /**
     * Proteins per 100g in milligrams
     */
    private Integer proteins;

    public LimitType getType() {
        return type;
    }

    public Integer getCalories() {
        return calories;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public Integer getFats() {
        return fats;
    }

    public Integer getProteins() {
        return proteins;
    }

    public Limit(LimitType type, Integer calories, Integer carbs, Integer fats, Integer proteins) {
        this.type = type;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
    }

    public static Limit getDefault(LimitType type) {
        switch (type) {
            case DAILY:
                return new Limit(type, DEFAULT_CALORIES_PER_DAY, DEFAULT_CARBS_PER_DAY, DEFAULT_FATS_PER_DAY, DEFAULT_PROTEINS_PER_DAY);
            case WEEKLY:
                return new Limit(type, 7*DEFAULT_CALORIES_PER_DAY, 7*DEFAULT_CARBS_PER_DAY, 7*DEFAULT_FATS_PER_DAY, 7*DEFAULT_PROTEINS_PER_DAY);
            default:
                throw new UnsupportedOperationException("Default limit for type " + type + " is not supported");
        }
    }
}
