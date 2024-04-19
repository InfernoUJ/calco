package com.example.calco.ui.charts.pie;

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

    public PieChartsPercents() {
        this(0,0,0,0);
    }
}
