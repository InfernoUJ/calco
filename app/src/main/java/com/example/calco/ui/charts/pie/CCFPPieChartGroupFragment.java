package com.example.calco.ui.charts.pie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.calco.databinding.FragmentCcfpPieChartGroupBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;

// CCFP - calories, carbs, fats, proteins
public class CCFPPieChartGroupFragment extends Fragment {
    private FragmentCcfpPieChartGroupBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCcfpPieChartGroupBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        setupPieChart(binding.pieChartCalories, new CaloriesPieChartData(50).getPieData());
        setupPieChart(binding.pieChartCarbs, new CarbsPieChartData(70).getPieData());
        setupPieChart(binding.pieChartFats, new FatsPieChartData(80).getPieData());
        setupPieChart(binding.pieChartProteins, new ProteinsPieChartData(30).getPieData());

        return root;
    }

    private void setupPieChart(PieChart pieChart, PieData pieData) {
        // todo
        pieChart.setData(pieData);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawCenterText(false);
        pieChart.getDescription().setEnabled(false);
        // this is for legend below chart
        pieChart.getLegend().setEnabled(false);
        pieChart.animate();
    }
}
