package com.example.calco.ui.charts.pie;

import android.graphics.Color;

public class CarbsPieData extends MacronutrientsPieData {
    public CarbsPieData(Integer chartLoading) {
        this.colors = new int[]{Color.GREEN, Color.GRAY};
        this.macronutrient = "Carbs";
        this.chartLoading = chartLoading > 100 ? 100 : chartLoading;
    }
}
