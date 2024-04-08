package com.example.calco.viewmodel.activity.state;

public class CreateProductUiState {
    private final String name;
    private final String calories;
    private final String carbs;
    private final String fats;
    private final String proteins;

    public CreateProductUiState(String name, String calories, String carbs, String fats, String proteins) {
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
    }
}
