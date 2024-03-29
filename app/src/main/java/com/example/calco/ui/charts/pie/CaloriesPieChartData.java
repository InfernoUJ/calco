package com.example.calco.ui.charts.pie;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.calco.R;

public class CaloriesPieChartData extends MacronutrientsPieChartData {
    public CaloriesPieChartData(Context context, Integer chartLoading) {
        super(context);
        this.colors = new int[] { ContextCompat.getColor(context, R.color.PIE_CHART_CALORIES),
                ContextCompat.getColor(context, R.color.PIE_CHART_BG) };
        this.macronutrient = "Carbs";
        setupPieChart(chartLoading);
    }
}
