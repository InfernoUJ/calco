package com.example.calco.ui.charts.pie;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChartViewModel extends ViewModel {
    private final MutableLiveData<PieData> mPieData;

    public PieChartViewModel() {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(18.5f, "Green"));
        pieEntries.add(new PieEntry(26.7f, "Yellow"));
        pieEntries.add(new PieEntry(24.0f, "Red"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Colors");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(40f);

        mPieData = new MutableLiveData<>(new PieData(pieDataSet));
    }

    public LiveData<PieData> getPieData() {
        return mPieData;
    }
}
