package com.example.calco.ui.charts.pie;

import android.graphics.Color;

public class CaloriesPieChartData extends MacronutrientsPieChartData {
    public CaloriesPieChartData(Integer chartLoading) {
        this.colors = new int[]{Color.RED, Color.GRAY};
        this.macronutrient = "Carbs";
        this.chartLoading = chartLoading > 100 ? 100 : chartLoading;
    }
}
