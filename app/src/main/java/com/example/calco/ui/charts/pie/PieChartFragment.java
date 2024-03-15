package com.example.calco.ui.charts.pie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calco.databinding.FragmentPieChartBinding;
import com.github.mikephil.charting.charts.PieChart;

public class PieChartFragment extends Fragment {
    private FragmentPieChartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PieChartViewModel galleryViewModel =
                new ViewModelProvider(this).get(PieChartViewModel.class);

        binding = FragmentPieChartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        PieChart pieChart = binding.pieChart;
        galleryViewModel.getPieData().observe(getViewLifecycleOwner(), pieData -> pieChart.setData(pieData));
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawCenterText(false);
        pieChart.getDescription().setEnabled(false);
        // this is for legend below chart
        pieChart.getLegend().setEnabled(false);
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
