package com.example.calco.ui.charts.pie;

import com.example.calco.logic.business.entities.Limit;

public class PieChartsPercents {
    public int caloriesPercent;
    public int carbsPercent;
    public int fatsPercent;
    public int proteinsPercent;

    public PieChartsPercents(int caloriesPercent, int carbsPercent, int fatsPercent, int proteinsPercent) {
        this.caloriesPercent = caloriesPercent;
        this.carbsPercent = carbsPercent;
        this.fatsPercent = fatsPercent;
        this.proteinsPercent = proteinsPercent;
    }

    public PieChartsPercents(int calories, int carbs, int fats, int proteins, Limit limit) {
        this(calories*100/ limit.getCalories(), carbs*100/ limit.getCarbs(), fats*100/limit.getFats(), proteins*100/limit.getProteins());
    }


    public PieChartsPercents() {
        this(0,0,0,0);
    }
}
