package com.example.calco.ui.charts.pie;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.calco.R;

public class CarbsPieChartData extends MacronutrientsPieChartData {
    public CarbsPieChartData(Context context, Integer chartLoading) {
        super(context);
        this.colors = new int[] { ContextCompat.getColor(context, R.color.PIE_CHART_CARBS),
                                 ContextCompat.getColor(context, R.color.PIE_CHART_BG) };
        this.macronutrient = "Carbs";
        setupPieChart(chartLoading);
    }
}
