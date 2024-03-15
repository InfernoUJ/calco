package com.example.calco.ui.charts.pie;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calco.databinding.FragmentPieChartBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.*;

public class PieChartFragment extends Fragment {
    private FragmentPieChartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PieChartViewModel galleryViewModel =
                new ViewModelProvider(this).get(PieChartViewModel.class);

        binding = FragmentPieChartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        PieChart pieChart = binding.pieChart;
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(18.5f, "Green"));
        pieEntries.add(new PieEntry(26.7f, "Yellow"));
        pieEntries.add(new PieEntry(24.0f, "Red"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Colors");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Colors");
        pieChart.animate();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
