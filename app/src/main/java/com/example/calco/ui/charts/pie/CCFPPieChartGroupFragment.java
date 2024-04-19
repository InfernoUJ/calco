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

        updateLoadings(10, 10, 10, 10);

        return root;
    }

    public void updateLoadings(int caloriesLoading, int carbsLoading, int fatsLoading, int proteinsLoading) {
        binding.pieChartCalories.clear();
        binding.pieChartCarbs.clear();
        binding.pieChartFats.clear();
        binding.pieChartProteins.clear();

        setupPieChart(binding.pieChartCalories, new CaloriesPieChartData(getContext(), caloriesLoading).getPieData());
        setupPieChart(binding.pieChartCarbs, new CarbsPieChartData(getContext(), carbsLoading).getPieData());
        setupPieChart(binding.pieChartFats, new FatsPieChartData(getContext(), fatsLoading).getPieData());
        setupPieChart(binding.pieChartProteins, new ProteinsPieChartData(getContext(), proteinsLoading).getPieData());
    }

    public void updateLoadings(PieChartsPercents percents) {
        updateLoadings(percents.caloriesPercent, percents.carbsPercent, percents.fatsPercent, percents.proteinsPercent);
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
//        pieChart.animate();
    }
}
