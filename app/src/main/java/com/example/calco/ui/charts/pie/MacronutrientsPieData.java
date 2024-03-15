package com.example.calco.ui.charts.pie;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class MacronutrientsPieData {
    protected Integer chartLoading;
    protected int[] colors;
    protected String macronutrient;
//    public void setupPieChart(Integer chartLoading) {
//        this.chartLoading = chartLoading > 100 ? 100 : chartLoading;
//    }
    public PieData getPieData() {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(chartLoading, ""));
        pieEntries.add(new PieEntry(100 - chartLoading, ""));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, macronutrient);
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(20f);

        return new PieData(pieDataSet);
    }
}
