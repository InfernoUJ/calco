package com.example.calco.ui.charts.pie;

import android.graphics.Color;

public class FatsPieChartData extends MacronutrientsPieChartData {
    public FatsPieChartData(Integer chartLoading) {
        this.colors = new int[]{Color.YELLOW, Color.GRAY};
        this.macronutrient = "Carbs";
        this.chartLoading = chartLoading > 100 ? 100 : chartLoading;
    }
}
