package com.example.calco.logic.business;

public abstract class Food {
    protected long id;
    protected String name;
    /**
     * Calories per 100g in cal
     */
    protected Integer calories;
    protected Integer carbs;
    protected Integer fats;
    protected Integer proteins;
    protected String imageName;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getImageName() {
        return imageName;
    }

    /*
     * image name must be without extension
     */
    public Food(long id, String name, Integer calories, Integer carbs, Integer fats, Integer proteins, String imageName) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
        this.imageName = imageName;
    }

}
