package com.example.calco.ui.charts.pie;

import android.graphics.Color;

public class ProteinsPieChartData extends MacronutrientsPieChartData {
    public ProteinsPieChartData(Integer chartLoading) {
        this.colors = new int[]{Color.BLUE, Color.GRAY};
        this.macronutrient = "Carbs";
        this.chartLoading = chartLoading > 100 ? 100 : chartLoading;
    }
}
