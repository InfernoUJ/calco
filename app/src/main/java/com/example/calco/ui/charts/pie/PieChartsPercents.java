package com.example.calco.ui.charts.pie;

import com.example.calco.logic.business.entities.Limit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class PieChartsPercents implements Serializable {
    private static final int MAX_PERCENTAGE = 100;
    public int caloriesPercent;
    public int carbsPercent;
    public int fatsPercent;
    public int proteinsPercent;

    public PieChartsPercents(int caloriesPercent, int carbsPercent, int fatsPercent, int proteinsPercent) {
        this.caloriesPercent = Math.min(caloriesPercent, MAX_PERCENTAGE);
        this.carbsPercent = Math.min(carbsPercent, MAX_PERCENTAGE);
        this.fatsPercent = Math.min(fatsPercent, MAX_PERCENTAGE);
        this.proteinsPercent = Math.min(proteinsPercent, MAX_PERCENTAGE);
    }

    public PieChartsPercents(int calories, int carbs, int fats, int proteins, Limit limit) {
        this(calories*100/ limit.getCalories(), carbs*100/ limit.getCarbs(), fats*100/limit.getFats(), proteins*100/limit.getProteins());
    }


    public PieChartsPercents() {
        this(0,0,0,0);
    }

    public ArrayList<Integer> toList() {
        return new ArrayList<>(Arrays.asList(caloriesPercent, carbsPercent, fatsPercent, proteinsPercent));
    }
}
